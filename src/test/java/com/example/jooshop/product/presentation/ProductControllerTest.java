package com.example.jooshop.product.presentation;

import com.example.jooshop.global.config.WebMvcConfig;
import com.example.jooshop.product.domain.Product;
import com.example.jooshop.product.dto.request.ProductCreateRequest;
import com.example.jooshop.product.dto.request.StockAddRequest;
import com.example.jooshop.product.dto.response.ProductResponse;
import com.example.jooshop.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(WebMvcConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 성공")
    void createProduct_success() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest("맥북 프로", 10);
        given(productService.createProduct(any(), anyInt())).willReturn(1L);

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/products/1"));
    }

    @Test
    @DisplayName("상품 등록 실패 - 상품명 없음")
    void createProduct_fail_noName() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest("", 10);

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 등록 실패 - 재고 음수")
    void createProduct_fail_negativeStock() throws Exception {
        // given
        ProductCreateRequest request = new ProductCreateRequest("맥북 프로", -1);

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 조회 성공")
    void getProduct_success() throws Exception {
        // given
        Product product = new Product("맥북 프로", 10);
        ProductResponse response = ProductResponse.from(product);
        given(productService.findById(1L)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("맥북 프로"))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    @DisplayName("상품 목록 조회 성공")
    void getProducts_success() throws Exception {
        // given
        Product product1 = new Product("맥북 프로", 10);
        Product product2 = new Product("아이패드", 20);
        List<ProductResponse> responses = List.of(
                ProductResponse.from(product1),
                ProductResponse.from(product2)
        );
        given(productService.findAll()).willReturn(responses);

        // when & then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("맥북 프로"))
                .andExpect(jsonPath("$[1].name").value("아이패드"));
    }

    @Test
    @DisplayName("재고 추가 성공")
    void addStock_success() throws Exception {
        // given
        StockAddRequest request = new StockAddRequest(5);

        // when & then
        mockMvc.perform(patch("/api/products/1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("재고 추가 실패 - 수량 0")
    void addStock_fail_zeroQuantity() throws Exception {
        // given
        StockAddRequest request = new StockAddRequest(0);

        // when & then
        mockMvc.perform(patch("/api/products/1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}