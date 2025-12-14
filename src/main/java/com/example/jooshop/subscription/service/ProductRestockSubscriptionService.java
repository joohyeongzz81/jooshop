package com.example.jooshop.subscription.service;

import com.example.jooshop.global.exception.BadRequestException;
import com.example.jooshop.product.domain.Product;
import com.example.jooshop.product.domain.repository.ProductRepository;
import com.example.jooshop.subscription.domain.ProductRestockSubscription;
import com.example.jooshop.subscription.domain.repository.ProductRestockSubscriptionRepository;
import com.example.jooshop.user.domain.User;
import com.example.jooshop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.jooshop.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductRestockSubscriptionService {

    private final ProductRestockSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long subscribe(final Long userId, final Long productId) {
        // 중복 구독 체크
        if (subscriptionRepository.existsByUserIdAndProductIdAndIsActiveTrue(userId, productId)) {
            throw new BadRequestException(DUPLICATE_SUBSCRIPTION);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_USER_ID));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_PRODUCT_ID));

        // 기존 구독이 있으면 재활성화, 없으면 새로 생성
        Optional<ProductRestockSubscription> existingSubscription =
                subscriptionRepository.findByUserIdAndProductId(userId, productId);

        if (existingSubscription.isPresent()) {
            ProductRestockSubscription subscription = existingSubscription.get();
            subscription.activate();
            return subscription.getId();
        }

        ProductRestockSubscription subscription = new ProductRestockSubscription(user, product);
        ProductRestockSubscription savedSubscription = subscriptionRepository.save(subscription);
        return savedSubscription.getId();
    }

    @Transactional
    public void unsubscribe(final Long userId, final Long productId) {
        ProductRestockSubscription subscription = subscriptionRepository
                .findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_SUBSCRIPTION_ID));

        subscription.deactivate();
    }
}