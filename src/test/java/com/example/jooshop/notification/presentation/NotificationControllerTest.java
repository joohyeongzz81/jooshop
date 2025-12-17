package com.example.jooshop.notification.presentation;

import com.example.jooshop.global.config.WebMvcConfig;
import com.example.jooshop.notification.dto.response.NotificationResponse;
import com.example.jooshop.notification.domain.NotificationType;
import com.example.jooshop.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(WebMvcConfig.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    @DisplayName("모든 알림 조회 성공")
    void getAllNotifications_success() throws Exception {
        // given
        Long userId = 1L;

        given(notificationService.findAllByUserId(userId))
                .willReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/notifications")
                        .header("X-User-Id", userId))
                .andExpect(status().isOk());

        verify(notificationService).findAllByUserId(userId);
    }

    @Test
    @DisplayName("안 읽은 알림만 조회 성공")
    void getUnreadNotifications_success() throws Exception {
        // given
        Long userId = 1L;

        given(notificationService.findUnreadByUserId(userId))
                .willReturn(List.of());

        // when & then
        mockMvc.perform(get("/api/notifications/unread")
                        .header("X-User-Id", userId))
                .andExpect(status().isOk());

        verify(notificationService).findUnreadByUserId(userId);
    }

    @Test
    @DisplayName("안 읽은 알림 개수 조회 성공")
    void getUnreadCount_success() throws Exception {
        // given
        Long userId = 1L;
        given(notificationService.countUnreadByUserId(userId)).willReturn(5L);

        // when & then
        mockMvc.perform(get("/api/notifications/unread/count")
                        .header("X-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));

        verify(notificationService).countUnreadByUserId(userId);
    }

    @Test
    @DisplayName("알림 읽음 처리 성공")
    void markAsRead_success() throws Exception {
        // given
        Long notificationId = 1L;

        // when & then
        mockMvc.perform(patch("/api/notifications/{notificationId}/read", notificationId))
                .andExpect(status().isNoContent());

        verify(notificationService).markAsRead(notificationId);
    }
}