package com.kkh.gateway.exception;

public class ErrorResponseFactory {
    public static ErrorResponse create(BaseErrorCode errorCode, String path) {
        return new ErrorResponse(
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                errorCode.getCode(),
                errorCode.getMessage(),
                path
        );
    }
}