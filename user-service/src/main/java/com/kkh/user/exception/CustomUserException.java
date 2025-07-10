package com.kkh.user.exception;

import lombok.Getter;

@Getter
public class CustomUserException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public CustomUserException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomUserException(BaseErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
