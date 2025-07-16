package com.kkh.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkh.user.exception.CommonErrorCode;
import com.kkh.user.exception.ErrorResponse;
import com.kkh.user.exception.ErrorResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        ErrorResponse errorResponse;
        int status;

        if (exception instanceof BadCredentialsException) {
            errorResponse = ErrorResponseFactory.create(CommonErrorCode.INVALID_PASSWORD, request.getRequestURI());
            status = CommonErrorCode.INVALID_PASSWORD.getStatus().value();
        } else if (exception instanceof UsernameNotFoundException) {
            errorResponse = ErrorResponseFactory.create(CommonErrorCode.USER_NOT_FOUND, request.getRequestURI());
            status = CommonErrorCode.USER_NOT_FOUND.getStatus().value();
        } else {
            errorResponse = ErrorResponseFactory.create(CommonErrorCode.UNAUTHORIZED, request.getRequestURI());
            status = CommonErrorCode.UNAUTHORIZED.getStatus().value();
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
