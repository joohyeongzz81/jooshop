package com.example.jooshop.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer stock = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Product(String name, Integer stock) {
        validateName(name);
        validateStock(stock);
        this.name = name;
        this.stock = stock;
    }

    public void addStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("추가 수량은 양수여야 합니다.");
        }
        this.stock += quantity;
    }

    public void decreaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("감소 수량은 양수여야 합니다.");
        }
        if (this.stock < quantity) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.stock -= quantity;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }
    }

    private void validateStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("재고는 0 이상이어야 합니다.");
        }
    }
}