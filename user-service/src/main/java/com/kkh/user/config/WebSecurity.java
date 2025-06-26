package com.kkh.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsService userDetailsService;

    public WebSecurity(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.csrf( (csrf) -> csrf.disable());

        http.authorizeHttpRequests((authHttpReq) -> authHttpReq
                .requestMatchers("/api/v1/users/welcome").permitAll() // 테스트용 API 예외처리
                .anyRequest().authenticated()
        ).authenticationManager(authenticationManager)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
