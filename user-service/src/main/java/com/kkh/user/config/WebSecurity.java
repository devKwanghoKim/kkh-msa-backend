package com.kkh.user.config;

import com.kkh.user.filter.CustomAuthenticationFilter;
import com.kkh.user.security.CustomAuthenticationProvider;
import com.kkh.user.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final JwtTokenProvider jwtTokenProvider;

    public WebSecurity(CustomAuthenticationProvider customAuthenticationProvider, JwtTokenProvider jwtTokenProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(customAuthenticationProvider);
        return builder.build();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((authHttpReq) -> authHttpReq
                        .requestMatchers("/welcome", "/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/me").access(
                                new WebExpressionAuthorizationManager(
                                        "hasIpAddress('127.0.0.1') or hasIpAddress('::1')")) // 내부망만 허용
                        .anyRequest().authenticated()
                ).authenticationManager(authManager)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 로그인 필터 추가
        http.addFilter(getAuthenticationFilter(authManager));

        return http.build();
    }

    private CustomAuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(authenticationManager, jwtTokenProvider);
        // /login(기본 경로) => /auth/login path 변경
        filter.setFilterProcessesUrl("/auth/login");
        return filter;
    }
}
