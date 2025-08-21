package com.kkh.user.exception;

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

    public static ErrorResponse create(BaseErrorCode errorCode, String path, String message) {
        return new ErrorResponse(
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                errorCode.getCode(),
                message,
                path
        );
    }
}