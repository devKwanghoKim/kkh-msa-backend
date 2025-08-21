package com.kkh.user.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private HttpStatus status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // 성공 응답
    // 기본 성공 응답 (status 200)
    public static <T> ApiResponse<T> success(T data) {
        return success(HttpStatus.OK, "요청 성공", data);
    }

    // 성공 응답 (status 200)
    public static <T> ApiResponse<T> success(T data, String message) {
        return success(HttpStatus.OK, message, data);
    }

    // 코드 + 메세지 커스텀
    public static <T> ApiResponse<T> success(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .build();
    }

    // 상태 코드 지정 가능 성공 응답
    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}