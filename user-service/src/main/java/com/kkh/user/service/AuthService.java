package com.kkh.user.service;

import com.kkh.user.domain.dto.UserRegistrationDto;
import com.kkh.user.domain.entity.User;
import com.kkh.user.domain.vo.Role;
import com.kkh.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void userRegistration(UserRegistrationDto userRegistrationDto) {
        String encodedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
        User user = User.builder()
                .username(userRegistrationDto.getUsername())
                .password(encodedPassword)
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
    }
}
