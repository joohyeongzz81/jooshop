package com.example.jooshop.product.domain.repository;

import com.example.jooshop.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}