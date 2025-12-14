package com.example.jooshop.subscription.presentation;

import com.example.jooshop.global.config.WebMvcConfig;
import com.example.jooshop.subscription.dto.request.ProductRestockSubscriptionRequest;
import com.example.jooshop.subscription.service.ProductRestockSubscriptionService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestockSubscriptionController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(WebMvcConfig.class)
class ProductRestockSubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductRestockSubscriptionService subscriptionService;

    @Test
    @DisplayName("재입고 알림 구독 성공")
    void subscribe_success() throws Exception {
        // given
        ProductRestockSubscriptionRequest request = new ProductRestockSubscriptionRequest(1L);
        given(subscriptionService.subscribe(anyLong(), anyLong())).willReturn(1L);

        // when & then
        mockMvc.perform(post("/api/restock-subscriptions")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/restock-subscriptions/1"));

        verify(subscriptionService).subscribe(1L, 1L);
    }

    @Test
    @DisplayName("재입고 알림 구독 실패 - 상품 ID 없음")
    void subscribe_fail_noProductId() throws Exception {
        // given
        ProductRestockSubscriptionRequest request = new ProductRestockSubscriptionRequest(null);

        // when & then
        mockMvc.perform(post("/api/restock-subscriptions")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("구독 취소 성공")
    void unsubscribe_success() throws Exception {
        // given
        ProductRestockSubscriptionRequest request = new ProductRestockSubscriptionRequest(1L);

        // when & then
        mockMvc.perform(delete("/api/restock-subscriptions")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(subscriptionService).unsubscribe(1L, 1L);
    }
}