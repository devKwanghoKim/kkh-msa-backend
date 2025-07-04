package com.kkh.user.config;

import com.kkh.user.security.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final CustomAuthenticationProvider customAuthenticationProvider;

    public WebSecurity(CustomAuthenticationProvider customAuthenticationProvider) {
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(customAuthenticationProvider);
        return builder.build();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http.csrf( (csrf) -> csrf.disable());

        http.authorizeHttpRequests((authHttpReq) -> authHttpReq
                .requestMatchers("/welcome", "/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/me").access(
                        new WebExpressionAuthorizationManager(
                                "hasIpAddress('127.0.0.1') or hasIpAddress('::1')")) // 내부망만 허용
                .anyRequest().authenticated()
        ).authenticationManager(authManager)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
