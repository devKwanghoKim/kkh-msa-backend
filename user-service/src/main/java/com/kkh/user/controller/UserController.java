package com.kkh.user.controller;

import com.kkh.user.common.api.ApiResponse;
import com.kkh.user.domain.dto.UserDto;
import com.kkh.user.domain.dto.UserUpdateDto;
import com.kkh.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getMyInfo(HttpServletRequest request) {
        String username = request.getHeader("X-User-Id");
        UserDto user = userService.getMyInfo(username);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<Void>> userUpdate(@Valid @RequestBody UserUpdateDto userUpdateDto , HttpServletRequest request) {
        String username = request.getHeader("X-User-Id");
        userService.userUpdate(username, userUpdateDto);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, "수정완료 되었습니다."));
    }
}
