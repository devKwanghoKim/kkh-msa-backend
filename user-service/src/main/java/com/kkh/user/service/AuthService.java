package com.kkh.user.service;

import com.kkh.user.domain.dto.LoginRequestDto;
import com.kkh.user.domain.dto.LoginResponseDto;
import com.kkh.user.domain.dto.UserRegistrationDto;
import com.kkh.user.domain.entity.User;
import com.kkh.user.domain.vo.Role;
import com.kkh.user.repository.UserRepository;
import com.kkh.user.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(), loginRequestDto.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);

        return LoginResponseDto.builder().accessToken(token).build();
    }

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
