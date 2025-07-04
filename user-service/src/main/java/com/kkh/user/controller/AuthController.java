package com.kkh.user.controller;

import com.kkh.user.domain.dto.LoginRequestDto;
import com.kkh.user.domain.dto.LoginResponseDto;
import com.kkh.user.domain.dto.UserRegistrationDto;
import com.kkh.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto userLoginResponseDto = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(userLoginResponseDto);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto userRegistrationDto) {
        authService.userRegistration(userRegistrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
    }
}
