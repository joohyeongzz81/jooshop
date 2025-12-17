package com.example.jooshop.notification.service;

import com.example.jooshop.global.exception.BadRequestException;
import com.example.jooshop.notification.domain.Notification;
import com.example.jooshop.notification.domain.NotificationType;
import com.example.jooshop.notification.domain.repository.NotificationRepository;
import com.example.jooshop.notification.dto.response.NotificationResponse;
import com.example.jooshop.product.domain.Product;
import com.example.jooshop.product.domain.repository.ProductRepository;
import com.example.jooshop.subscription.domain.ProductRestockSubscription;
import com.example.jooshop.subscription.domain.repository.ProductRestockSubscriptionRepository;
import com.example.jooshop.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ProductRestockSubscriptionRepository subscriptionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("재입고 알림 발송 성공")
    void sendRestockNotification_success() {
        // given
        Long productId = 1L;
        Product product = new Product("맥북 프로", 10);
        ReflectionTestUtils.setField(product, "id", productId);

        User user1 = new User("user1@example.com", "유저1");
        User user2 = new User("user2@example.com", "유저2");

        ProductRestockSubscription subscription1 = new ProductRestockSubscription(user1, product);
        ProductRestockSubscription subscription2 = new ProductRestockSubscription(user2, product);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(subscriptionRepository.findByProductIdAndIsActiveTrue(productId))
                .willReturn(List.of(subscription1, subscription2));

        // when
        notificationService.sendRestockNotification(productId);

        // then
        verify(productRepository).findById(productId);
        verify(subscriptionRepository).findByProductIdAndIsActiveTrue(productId);
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 재입고 알림 발송 실패")
    void sendRestockNotification_fail_productNotFound() {
        // given
        Long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> notificationService.sendRestockNotification(productId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("요청한 ID에 해당하는 상품이 존재하지 않습니다.");

        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("구독자가 없는 경우 알림 발송하지 않음")
    void sendRestockNotification_noSubscribers() {
        // given
        Long productId = 1L;
        Product product = new Product("맥북 프로", 10);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(subscriptionRepository.findByProductIdAndIsActiveTrue(productId))
                .willReturn(List.of());

        // when
        notificationService.sendRestockNotification(productId);

        // then
        verify(notificationRepository, times(0)).save(any(Notification.class));
    }

    @Test
    @DisplayName("유저의 모든 알림 조회 성공")
    void findAllByUserId_success() {
        // given
        Long userId = 1L;
        User user = new User("test@example.com", "홍길동");

        Notification notification1 = new Notification(
                user, NotificationType.PRODUCT_RESTOCK,
                "맥북 프로 재입고!", "관심 상품이 재입고되었습니다.", 1L
        );
        given(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId))
                .willReturn(List.of(notification1));

        // when
        List<NotificationResponse> responses = notificationService.findAllByUserId(userId);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getTitle()).isEqualTo("맥북 프로 재입고!");
        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Test
    @DisplayName("안 읽은 알림만 조회 성공")
    void findUnreadByUserId_success() {
        // given
        Long userId = 1L;
        User user = new User("test@example.com", "홍길동");

        Notification notification = new Notification(
                user, NotificationType.PRODUCT_RESTOCK,
                "맥북 프로 재입고!", "관심 상품이 재입고되었습니다.", 1L
        );

        given(notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId))
                .willReturn(List.of(notification));

        // when
        List<NotificationResponse> responses = notificationService.findUnreadByUserId(userId);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getIsRead()).isFalse();
        verify(notificationRepository).findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    @Test
    @DisplayName("안 읽은 알림 개수 조회 성공")
    void countUnreadByUserId_success() {
        // given
        Long userId = 1L;
        given(notificationRepository.countByUserIdAndIsReadFalse(userId)).willReturn(5L);

        // when
        long count = notificationService.countUnreadByUserId(userId);

        // then
        assertThat(count).isEqualTo(5L);
        verify(notificationRepository).countByUserIdAndIsReadFalse(userId);
    }

    @Test
    @DisplayName("알림 읽음 처리 성공")
    void markAsRead_success() {
        // given
        Long notificationId = 1L;
        User user = new User("test@example.com", "홍길동");
        Notification notification = new Notification(
                user, NotificationType.PRODUCT_RESTOCK,
                "맥북 프로 재입고!", "관심 상품이 재입고되었습니다.", 1L
        );

        given(notificationRepository.findById(notificationId))
                .willReturn(Optional.of(notification));

        // when
        notificationService.markAsRead(notificationId);

        // then
        assertThat(notification.getIsRead()).isTrue();
        assertThat(notification.getReadAt()).isNotNull();
        verify(notificationRepository).findById(notificationId);
    }

    @Test
    @DisplayName("존재하지 않는 알림 읽음 처리 실패")
    void markAsRead_fail_notFound() {
        // given
        Long notificationId = 1L;
        given(notificationRepository.findById(notificationId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> notificationService.markAsRead(notificationId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("요청한 ID에 해당하는 알림이 존재하지 않습니다.");

        verify(notificationRepository).findById(notificationId);
    }
}