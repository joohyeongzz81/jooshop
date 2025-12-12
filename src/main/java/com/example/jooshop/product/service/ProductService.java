package com.example.jooshop.product.service;

import com.example.jooshop.product.domain.Product;
import com.example.jooshop.product.domain.repository.ProductRepository;
import com.example.jooshop.product.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Long createProduct(String name, Integer stock) {
        Product product = new Product(name, stock);
        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }

    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        return ProductResponse.from(product);
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional
    public void addStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        product.addStock(quantity);
        // TODO: 재입고 알림 발송 로직 추가
    }

    @Transactional
    public void decreaseStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        product.decreaseStock(quantity);
    }
}