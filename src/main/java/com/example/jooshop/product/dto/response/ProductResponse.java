package com.example.jooshop.product.dto.response;

import com.example.jooshop.product.domain.Product;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductResponse {

    private final Long id;
    private final String name;
    private final Integer stock;
    private final LocalDateTime createdAt;

    private ProductResponse(Long id, String name, Integer stock, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getStock(),
                product.getCreatedAt()
        );
    }
}