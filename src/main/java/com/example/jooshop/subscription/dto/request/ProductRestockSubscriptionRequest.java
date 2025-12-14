package com.example.jooshop.subscription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRestockSubscriptionRequest {

    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    public ProductRestockSubscriptionRequest(final Long productId) {
        this.productId = productId;
    }
}