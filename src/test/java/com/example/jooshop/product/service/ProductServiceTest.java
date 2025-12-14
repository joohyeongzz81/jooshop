package com.example.jooshop.product.service;

import com.example.jooshop.global.exception.BadRequestException;
import com.example.jooshop.product.domain.Product;
import com.example.jooshop.product.domain.repository.ProductRepository;
import com.example.jooshop.product.dto.request.ProductCreateRequest;
import com.example.jooshop.product.dto.request.StockAddRequest;
import com.example.jooshop.product.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 성공")
    void createProduct_success() {
        // given
        ProductCreateRequest request = new ProductCreateRequest("맥북 프로", 10);
        Product savedProduct = new Product(request.getName(), request.getStock());
        ReflectionTestUtils.setField(savedProduct, "id", 1L);

        given(productRepository.save(any(Product.class))).willReturn(savedProduct);

        // when
        Long productId = productService.createProduct(request);

        // then
        assertThat(productId).isNotNull();
        assertThat(productId).isEqualTo(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 조회 성공")
    void findById_success() {
        // given
        Long productId = 1L;
        Product product = new Product("맥북 프로", 10);
        ReflectionTestUtils.setField(product, "id", productId);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        ProductResponse response = productService.findById(productId);

        // then
        assertThat(response.getName()).isEqualTo("맥북 프로");
        assertThat(response.getStock()).isEqualTo(10);
        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 시 예외 발생")
    void findById_fail_notFound() {
        // given
        Long productId = 1L;
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.findById(productId))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("요청한 ID에 해당하는 상품이 존재하지 않습니다.");

        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("상품 목록 조회 성공")
    void findAll_success() {
        // given
        Product product1 = new Product("맥북 프로", 10);
        Product product2 = new Product("아이패드", 20);

        given(productRepository.findAll()).willReturn(List.of(product1, product2));

        // when
        List<ProductResponse> products = productService.findAll();

        // then
        assertThat(products).hasSize(2);
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("재고 추가 성공")
    void addStock_success() {
        // given
        Long productId = 1L;
        StockAddRequest request = new StockAddRequest(5);
        Product product = new Product("맥북 프로", 10);
        ReflectionTestUtils.setField(product, "id", productId);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        productService.addStock(productId, request);

        // then
        assertThat(product.getStock()).isEqualTo(15);
        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("재고 감소 성공")
    void decreaseStock_success() {
        // given
        Long productId = 1L;
        Product product = new Product("맥북 프로", 10);
        ReflectionTestUtils.setField(product, "id", productId);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        productService.decreaseStock(productId, 3);

        // then
        assertThat(product.getStock()).isEqualTo(7);
        verify(productRepository).findById(productId);
    }

    @Test
    @DisplayName("재고 부족 시 감소 실패")
    void decreaseStock_fail_insufficientStock() {
        // given
        Long productId = 1L;
        Product product = new Product("맥북 프로", 5);
        ReflectionTestUtils.setField(product, "id", productId);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when & then
        assertThatThrownBy(() -> productService.decreaseStock(productId, 10))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("재고가 부족합니다.");

        verify(productRepository).findById(productId);
    }
}