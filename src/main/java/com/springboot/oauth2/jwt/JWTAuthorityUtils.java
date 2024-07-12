package com.springboot.oauth2.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthorityUtils {
    @Value("${mail.address.admin}")
    private String adminMailAddess;

    private final List<GrantedAuthority> ADMIN_ROLES =
            AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
    private final List<GrantedAuthority> USER_ROLES =
            AuthorityUtils.createAuthorityList("ROLE_USER");

    //여기선 ROLE_빼주기!
    private final List<String> ADMIN_ROLES_STRING =
            List.of("ADMIN", "USER");
    private final List<String> USER_ROLES_STRING =
            List.of("USER");

    public List<GrantedAuthority> createAuthorities(String email){
        if (email.equals(adminMailAddess))
            return ADMIN_ROLES;
        return USER_ROLES;
    }

    public List<GrantedAuthority> createAuthorities(List<String> roles){
        List<GrantedAuthority> authorities =
                roles.stream()
                        // ROLE_꼭 추가하기
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

        return authorities;
    }

    public List<String> createRoles(String email){
        if(email.equals(adminMailAddess))
            return ADMIN_ROLES_STRING;
        return USER_ROLES_STRING;
    }
}
