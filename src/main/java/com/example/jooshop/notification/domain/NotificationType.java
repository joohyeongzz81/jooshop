package com.example.jooshop.notification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {

    PRODUCT_RESTOCK("상품 재입고");

    private final String description;
}