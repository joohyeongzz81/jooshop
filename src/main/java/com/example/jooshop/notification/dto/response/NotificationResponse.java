package com.example.jooshop.notification.dto.response;

import com.example.jooshop.notification.domain.Notification;
import com.example.jooshop.notification.domain.NotificationType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {

    private final Long id;
    private final NotificationType type;
    private final String title;
    private final String content;
    private final Long referenceId;
    private final Boolean isRead;
    private final LocalDateTime readAt;
    private final LocalDateTime createdAt;

    private NotificationResponse(
            final Long id,
            final NotificationType type,
            final String title,
            final String content,
            final Long referenceId,
            final Boolean isRead,
            final LocalDateTime readAt,
            final LocalDateTime createdAt
    ) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.referenceId = referenceId;
        this.isRead = isRead;
        this.readAt = readAt;
        this.createdAt = createdAt;
    }

    public static NotificationResponse from(final Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getContent(),
                notification.getReferenceId(),
                notification.getIsRead(),
                notification.getReadAt(),
                notification.getCreatedAt()
        );
    }
}