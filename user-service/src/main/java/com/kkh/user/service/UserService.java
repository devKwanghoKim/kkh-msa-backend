package com.kkh.user.service;

import com.kkh.user.domain.dto.UserDto;
import com.kkh.user.domain.dto.UserUpdateDto;
import com.kkh.user.domain.entity.User;
import com.kkh.user.repository.UserRepository;
import com.kkh.user.security.CustomUserDetails;
import com.kkh.user.security.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;

    public UserDto getMyInfo(String username) {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        return UserDto.builder()
                .email(userDetails.getUserEmail())
                .nickname(userDetails.getUserNickname())
                .username(userDetails.getUsername())
                .phoneNumber(userDetails.getUserPhoneNumber())
                .build();
    }

    @Transactional
    public void userUpdate(String username, UserUpdateDto userUpdateDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.updateProfile(userUpdateDto.getNickname(), userUpdateDto.getEmail(), userUpdateDto.getPhoneNumber());
    }
}
