package com.example.jooshop.product.presentation;

import com.example.jooshop.product.dto.request.ProductCreateRequest;
import com.example.jooshop.product.dto.request.StockAddRequest;
import com.example.jooshop.product.dto.response.ProductResponse;
import com.example.jooshop.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody final ProductCreateRequest request) {
        Long productId = productService.createProduct(request);
        return ResponseEntity.created(URI.create("/products/" + productId))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable final Long id) {
        ProductResponse response = productService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        List<ProductResponse> responses = productService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> addStock(
            @PathVariable final Long id,
            @Valid @RequestBody final StockAddRequest request) {
        productService.addStock(id, request);
        return ResponseEntity.ok().build();
    }
}