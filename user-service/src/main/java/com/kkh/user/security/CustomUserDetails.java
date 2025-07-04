package com.kkh.user.security;

import com.kkh.user.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * 사용자의 권한 목록을 반환합니다.
     * Spring Security는 ROLE_ 접두사가 붙은 문자열을 권한으로 인식합니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    /**
     * 사용자의 비밀번호를 반환합니다.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 사용자의 이름(아이디)을 반환합니다.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 계정이 만료되지 않았는지를 나타냅니다.
     * true이면 계정이 유효함을 의미합니다.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겨있지 않은지를 나타냅니다.
     * true이면 계정이 잠겨있지 않음을 의미합니다.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명이 만료되지 않았는지를 나타냅니다.
     * true이면 비밀번호가 유효함을 의미합니다.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 활성화되었는지를 나타냅니다.
     * true이면 계정이 사용 가능함을 의미합니다.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
