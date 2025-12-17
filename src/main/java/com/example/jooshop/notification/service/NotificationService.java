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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.jooshop.global.exception.ExceptionCode.NOT_FOUND_NOTIFICATION_ID;
import static com.example.jooshop.global.exception.ExceptionCode.NOT_FOUND_PRODUCT_ID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProductRestockSubscriptionRepository subscriptionRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void sendRestockNotification(final Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_PRODUCT_ID));

        // 활성 구독자 조회
        List<ProductRestockSubscription> subscriptions =
                subscriptionRepository.findByProductIdAndIsActiveTrue(productId);

        // 각 구독자에게 알림 생성
        subscriptions.forEach(subscription -> {
            Notification notification = new Notification(
                    subscription.getUser(),
                    NotificationType.PRODUCT_RESTOCK,
                    product.getName() + " 재입고!",
                    "관심 상품이 재입고되었습니다. 지금 바로 확인해보세요!",
                    productId
            );
            notificationRepository.save(notification);
        });
    }

    public List<NotificationResponse> findAllByUserId(final Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationResponse::from)
                .toList();
    }

    public List<NotificationResponse> findUnreadByUserId(final Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId).stream()
                .map(NotificationResponse::from)
                .toList();
    }

    public long countUnreadByUserId(final Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(final Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_NOTIFICATION_ID));

        notification.markAsRead();
    }
}