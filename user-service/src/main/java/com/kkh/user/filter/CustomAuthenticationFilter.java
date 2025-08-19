package com.kkh.user.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkh.user.domain.dto.LoginRequestDto;
import com.kkh.user.domain.dto.UserDto;
import com.kkh.user.security.JwtTokenProvider;
import com.kkh.user.service.RedisTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTokenService redisTokenService;
    private final Long refreshExpiration;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                                      RedisTokenService redisTokenService, Long refreshExpiration) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTokenService = redisTokenService;
        this.refreshExpiration = refreshExpiration;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            // JSON → DTO 변환
            LoginRequestDto creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication authentication) throws IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        redisTokenService.saveAccessToken(userDetails.getUsername(), accessToken);
        redisTokenService.saveRefreshToken(userDetails.getUsername(), refreshToken);

        res.addHeader("token", accessToken);
        res.addHeader("userId", userDetails.getUsername());


        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        // refreshTokenCookie.setSecure(true); // TODO: 서버 환경에 따른 분기 추가 필요, https 환경이면 true, 아니면 false
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) (refreshExpiration / 1000)); // 초 단위로 변환
        // refreshTokenCookie.setAttribute("SameSite", "None"); // TODO: 서버 환경에 따른 분기 추가 필요, https 환경이면 none, 아니면 Lax
        refreshTokenCookie.setAttribute("SameSite", "Lax");
        res.addCookie(refreshTokenCookie);

        UserDto userDto = new UserDto(
                userDetails.getUsername(),
                "임시",
                userDetails.getUsername() + "@example.com"
        );

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(res.getWriter(), userDto);
    }
}
