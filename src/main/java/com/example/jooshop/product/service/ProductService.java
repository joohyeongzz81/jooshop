package com.example.jooshop.product.service;

import com.example.jooshop.product.domain.Product;
import com.example.jooshop.product.domain.repository.ProductRepository;
import com.example.jooshop.product.dto.request.ProductCreateRequest;
import com.example.jooshop.product.dto.request.StockAddRequest;
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
    public Long createProduct(final ProductCreateRequest request) {
        Product product = new Product(request.getName(), request.getStock());
        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }

    public ProductResponse findById(final Long id) {
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
    public void addStock(final Long productId, final StockAddRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        product.addStock(request.getQuantity());
        // TODO: 재입고 알림 발송 로직 추가
    }

    @Transactional
    public void decreaseStock(final Long productId, final Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
        product.decreaseStock(quantity);
    }
}