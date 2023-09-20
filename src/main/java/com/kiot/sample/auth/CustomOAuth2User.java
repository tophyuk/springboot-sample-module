package com.kiot.sample.auth;

import com.kiot.sample.domain.Role;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


@Data
@NoArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private Map<String, Object> attributes;
    private String name;
    private String password;
    private Role role;
    private String tokenValue;
    private String loginType;

    public CustomOAuth2User(Map<String, Object> attributes, String name, String password, Role role, String tokenValue, String loginType) {
        this.attributes = attributes;
        this.name = name;
        this.password = password;
        this.role = role;
        this.tokenValue = tokenValue;
        this.loginType = loginType;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getKey()));
        return authorities;
    }

    @Override
    public String getName() {
        return name;
    }
}
