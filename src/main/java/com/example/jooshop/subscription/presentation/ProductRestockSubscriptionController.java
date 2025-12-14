package com.example.jooshop.subscription.presentation;

import com.example.jooshop.subscription.dto.request.ProductRestockSubscriptionRequest;
import com.example.jooshop.subscription.service.ProductRestockSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/restock-subscriptions")
@RequiredArgsConstructor
public class ProductRestockSubscriptionController {

    private final ProductRestockSubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<Void> subscribe(
            @Valid @RequestBody final ProductRestockSubscriptionRequest request,
            @RequestHeader("X-User-Id") final Long userId  // TODO: Spring Security로 변경
    ) {
        Long subscriptionId = subscriptionService.subscribe(userId, request.getProductId());
        return ResponseEntity.created(URI.create("/restock-subscriptions/" + subscriptionId))
                .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> unsubscribe(
            @Valid @RequestBody final ProductRestockSubscriptionRequest request,
            @RequestHeader("X-User-Id") final Long userId  // TODO: Spring Security로 변경
    ) {
        subscriptionService.unsubscribe(userId, request.getProductId());
        return ResponseEntity.noContent().build();
    }
}