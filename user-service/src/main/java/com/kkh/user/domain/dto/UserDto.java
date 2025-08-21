package com.kkh.user.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String nickname;
    private String email;
    private String phoneNumber;
}
