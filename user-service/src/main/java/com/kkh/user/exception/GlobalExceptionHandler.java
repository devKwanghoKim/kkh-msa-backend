package com.kkh.user.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // user 커스텀 예외
    @ExceptionHandler(CustomUserException.class)
    public ResponseEntity<ErrorResponse> handleCustomUserException(CustomUserException ex, HttpServletRequest request) {
        BaseErrorCode code = ex.getErrorCode();
        log.warn("User 예외 발생: {}", code.getMessage());
        return buildErrorResponse(code, request.getRequestURI());
    }
    // 서버 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("서버 내부 오류 발생", ex);
        return buildErrorResponse(CommonErrorCode.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }

    // 필수값 누락
    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<ErrorResponse> handlePropertyValueException(PropertyValueException ex, HttpServletRequest request) {
        log.warn("엔티티 필드 누락 예외 발생: {}", ex.getMessage());
        return buildErrorResponse(CommonErrorCode.INVALID_INPUT, request.getRequestURI());
    }

    // DB 제약 조건 위반
    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleDBConstraintException(Exception ex, HttpServletRequest request) {
        log.warn("DB 제약 조건 위반 발생: {}", ex.getMessage());
        return buildErrorResponse(CommonErrorCode.VALIDATION_ERROR, request.getRequestURI());
    }

    // DTO 검증 실패 예외 처리 (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                    HttpServletRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse(CommonErrorCode.INVALID_INPUT.getMessage());
        log.warn("유효성 검증 실패: {}", errorMessage);
        return buildErrorResponse(CommonErrorCode.INVALID_INPUT, request.getRequestURI(), errorMessage);
    }

    /* 그외 예외 추가 필요 */
    private ResponseEntity<ErrorResponse> buildErrorResponse(BaseErrorCode code, String path) {
        ErrorResponse response = ErrorResponseFactory.create(code, path);
        return new ResponseEntity<>(response, code.getStatus());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(BaseErrorCode code, String path, String errorMessage) {
        ErrorResponse response = ErrorResponseFactory.create(code, path, errorMessage);
        return new ResponseEntity<>(response, code.getStatus());
    }
}