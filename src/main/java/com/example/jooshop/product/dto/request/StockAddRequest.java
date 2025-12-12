package com.example.jooshop.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StockAddRequest {

    @NotNull(message = "추가 수량은 필수입니다.")
    @Min(value = 1, message = "추가 수량은 1 이상이어야 합니다.")
    private Integer quantity;

    public StockAddRequest(Integer quantity) {
        this.quantity = quantity;
    }
}