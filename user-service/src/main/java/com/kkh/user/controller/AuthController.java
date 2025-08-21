package com.kkh.user.controller;

import com.kkh.user.common.api.ApiResponse;
import com.kkh.user.domain.dto.UserRegistrationDto;
import com.kkh.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        authService.userRegistration(userRegistrationDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED, "회원 가입 완료"));
    }
}
