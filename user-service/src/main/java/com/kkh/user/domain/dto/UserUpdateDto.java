package com.kkh.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateDto {
    @NotBlank(message = "닉네임은 필수 값 입니다.")
    private String nickname;
    @NotBlank(message = "email은 필수 값 입니다.")
    private String email;
    private String phoneNumber;
}
