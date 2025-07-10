package com.kkh.user.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<ErrorResponse> handleCustomUserException(CustomUserException ex, HttpServletRequest request) {
        BaseErrorCode code = ex.getErrorCode();
        log.warn("User 예외 발생: {}", code.getMessage());
        return buildErrorResponse(code, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("서버 내부 오류 발생", ex);
        return buildErrorResponse(CommonErrorCode.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(BaseErrorCode code, String path) {
        ErrorResponse response = ErrorResponseFactory.create(code, path);
        return new ResponseEntity<>(response, code.getStatus());
    }
}