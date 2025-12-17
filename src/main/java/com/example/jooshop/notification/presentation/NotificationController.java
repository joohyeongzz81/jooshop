package com.example.jooshop.notification.presentation;

import com.example.jooshop.notification.dto.response.NotificationResponse;
import com.example.jooshop.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(
            @RequestHeader("X-User-Id") final Long userId  // TODO: Spring Security로 변경
    ) {
        List<NotificationResponse> notifications = notificationService.findAllByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @RequestHeader("X-User-Id") final Long userId  // TODO: Spring Security로 변경
    ) {
        List<NotificationResponse> notifications = notificationService.findUnreadByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount(
            @RequestHeader("X-User-Id") final Long userId  // TODO: Spring Security로 변경
    ) {
        long count = notificationService.countUnreadByUserId(userId);
        return ResponseEntity.ok(count);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable final Long notificationId
    ) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }
}