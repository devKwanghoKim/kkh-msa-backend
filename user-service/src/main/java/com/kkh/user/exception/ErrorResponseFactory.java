package com.kkh.user.exception;

import java.time.LocalDateTime;

public class ErrorResponseFactory {
    public static ErrorResponse create(BaseErrorCode errorCode, String path) {
        return new ErrorResponse(
                LocalDateTime.now(),
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                errorCode.getCode(),
                errorCode.getMessage(),
                path
        );
    }
}