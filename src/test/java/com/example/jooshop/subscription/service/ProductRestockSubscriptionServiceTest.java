package com.example.jooshop.subscription.service;

import com.example.jooshop.global.exception.BadRequestException;
import com.example.jooshop.product.domain.Product;
import com.example.jooshop.product.domain.repository.ProductRepository;
import com.example.jooshop.subscription.domain.ProductRestockSubscription;
import com.example.jooshop.subscription.domain.repository.ProductRestockSubscriptionRepository;
import com.example.jooshop.user.domain.User;
import com.example.jooshop.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductRestockSubscriptionServiceTest {

    @Mock
    private ProductRestockSubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductRestockSubscriptionService subscriptionService;

    @Test
    @DisplayName("재입고 알림 구독 성공")
    void subscribe_success() {
        // given
        Long userId = 1L;
        Long productId = 1L;

        User user = new User("test@example.com", "홍길동");
        ReflectionTestUtils.setField(user, "id", userId);

        Product product = new Product("맥북 프로", 0);
        ReflectionTestUtils.setField(product, "id", productId);

        ProductRestockSubscription savedSubscription = new ProductRestockSubscription(user, product);
        ReflectionTestUtils.setField(savedSubscription, "id", 1L);

        given(subscriptionRepository.existsByUserIdAndProductIdAndIsActiveTrue(userId, productId))
                .willReturn(false);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(subscriptionRepository.findByUserIdAndProductId(userId, productId))
                .willReturn(Optional.empty());
        given(subscriptionRepository.save(any(ProductRestockSubscription.class)))
                .willReturn(savedSubscription);

        // when
        Long subscriptionId = subscriptionService.subscribe(userId, productId);

        // then
        assertThat(subscriptionId).isNotNull();
        assertThat(subscriptionId).isEqualTo(1L);
        verify(subscriptionRepository).existsByUserIdAndProductIdAndIsActiveTrue(userId, productId);
        verify(userRepository).findById(userId);
        verify(productRepository).findById(productId);
        verify(subscriptionRepository).save(any(ProductRestockSubscription.class));
    }

    @Test
    @DisplayName("이미 구독 중인 경우 구독 실패")
    void subscribe_fail_duplicateSubscription() {
        // given
        Long userId = 1L;
        Long productId = 1L;

        given(subscriptionRepository.existsByUserIdAndProductIdAndIsActiveTrue(userId, productId))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> subscriptionService.subscribe(userId, productId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이미 구독 중인 상품입니다.");

        verify(subscriptionRepository).existsByUserIdAndProductIdAndIsActiveTrue(userId, productId);
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 구독 실패")
    void subscribe_fail_userNotFound() {
        // given
        Long userId = 1L;
        Long productId = 1L;

        given(subscriptionRepository.existsByUserIdAndProductIdAndIsActiveTrue(userId, productId))
                .willReturn(false);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> subscriptionService.subscribe(userId, productId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("요청한 ID에 해당하는 회원이 존재하지 않습니다.");

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 구독 실패")
    void subscribe_fail_productNotFound() {
        // given
        Long userId = 1L;
        Long productId = 1L;

        User user = new User("test@example.com", "홍길동");
        ReflectionTestUtils.setField(user, "id", userId);

        given(subscriptionRepository.existsByUserIdAndProductIdAndIsActiveTrue(userId, productId))
                .willReturn(false);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> subscriptionService.subscribe(userId, productId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("요청한 ID에 해당하는 상품이 존재하지 않습니다.");

        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("비활성 구독 재활성화 성공")
    void subscribe_success_reactivate() {
        // given
        Long userId = 1L;
        Long productId = 1L;

        User user = new User("test@example.com", "홍길동");
        ReflectionTestUtils.setField(user, "id", userId);

        Product product = new Product("맥북 프로", 0);
        ReflectionTestUtils.setField(product, "id", productId);

        ProductRestockSubscription existingSubscription = new ProductRestockSubscription(user, product);
        ReflectionTestUtils.setField(existingSubscription, "id", 1L);
        existingSubscription.deactivate();

        given(subscriptionRepository.existsByUserIdAndProductIdAndIsActiveTrue(userId, productId))
                .willReturn(false);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(subscriptionRepository.findByUserIdAndProductId(userId, productId))
                .willReturn(Optional.of(existingSubscription));

        // when
        Long subscriptionId = subscriptionService.subscribe(userId, productId);

        // then
        assertThat(subscriptionId).isEqualTo(1L);
        assertThat(existingSubscription.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("구독 취소 성공")
    void unsubscribe_success() {
        // given
        Long userId = 1L;
        Long productId = 1L;

        User user = new User("test@example.com", "홍길동");
        Product product = new Product("맥북 프로", 0);
        ProductRestockSubscription subscription = new ProductRestockSubscription(user, product);

        given(subscriptionRepository.findByUserIdAndProductId(userId, productId))
                .willReturn(Optional.of(subscription));

        // when
        subscriptionService.unsubscribe(userId, productId);

        // then
        assertThat(subscription.getIsActive()).isFalse();
        verify(subscriptionRepository).findByUserIdAndProductId(userId, productId);
    }

    @Test
    @DisplayName("존재하지 않는 구독 취소 실패")
    void unsubscribe_fail_notFound() {
        // given
        Long userId = 1L;
        Long productId = 1L;

        given(subscriptionRepository.findByUserIdAndProductId(userId, productId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> subscriptionService.unsubscribe(userId, productId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("요청한 ID에 해당하는 구독이 존재하지 않습니다.");

        verify(subscriptionRepository).findByUserIdAndProductId(userId, productId);
    }
}