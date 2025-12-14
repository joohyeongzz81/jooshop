package com.example.jooshop.product.domain;

import com.example.jooshop.global.entity.BaseEntity;
import com.example.jooshop.global.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.jooshop.global.exception.ExceptionCode.INSUFFICIENT_STOCK;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer stock = 0;

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
            throw new BadRequestException(INSUFFICIENT_STOCK);
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