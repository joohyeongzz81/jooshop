package com.example.jooshop.subscription.domain.repository;

import com.example.jooshop.subscription.domain.ProductRestockSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRestockSubscriptionRepository extends JpaRepository<ProductRestockSubscription, Long> {

    List<ProductRestockSubscription> findByProductIdAndIsActiveTrue(Long productId);

    Optional<ProductRestockSubscription> findByUserIdAndProductId(Long userId, Long productId);

    boolean existsByUserIdAndProductIdAndIsActiveTrue(Long userId, Long productId);
}