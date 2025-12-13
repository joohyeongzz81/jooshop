package com.example.jooshop.global.exception;

public class BadRequestException extends BusinessException {

    public BadRequestException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}