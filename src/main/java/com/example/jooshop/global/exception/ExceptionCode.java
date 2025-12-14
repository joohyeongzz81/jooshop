package com.example.jooshop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    INVALID_REQUEST(1000, "올바르지 않은 요청입니다."),

    // User (1001~1099)
    NOT_FOUND_USER_ID(1001, "요청한 ID에 해당하는 회원이 존재하지 않습니다."),
    DUPLICATE_EMAIL(1002, "이미 존재하는 이메일입니다."),

    // Product (2001~2099)
    NOT_FOUND_PRODUCT_ID(2001, "요청한 ID에 해당하는 상품이 존재하지 않습니다."),
    INSUFFICIENT_STOCK(2002, "재고가 부족합니다."),

    NOT_FOUND_SUBSCRIPTION_ID(4001, "요청한 ID에 해당하는 구독이 존재하지 않습니다."),
    DUPLICATE_SUBSCRIPTION(4002, "이미 구독 중인 상품입니다."),

    INTERNAL_SERVER_ERROR(9999, "서버 에러가 발생하였습니다. 관리자에게 문의해 주세요.");

    private final int code;
    private final String message;
}