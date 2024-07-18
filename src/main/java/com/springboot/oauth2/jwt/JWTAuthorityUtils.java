package com.springboot.oauth2.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

    public String getRefreshToken(HttpServletRequest req) {
        if (req.getCookies() != null){
            System.out.println(req.getCookies());
            for (Cookie c : req.getCookies()) {
                if (c.getName().equals("refreshtoken"))
                    return c.getValue();
            }
        }

        return null;
    }

    public Cookie createCookie(String userName, String refreshToken) {
        String cookieName = "refreshtoken";
        String cookieValue = refreshToken; //
        Cookie cookie = new Cookie(cookieName, cookieValue);
        // 쿠키 속성 설정
        cookie.setHttpOnly(true);  //httponly 옵션 설정
        cookie.setSecure(true); //https 옵션 설정(디폴트 false/요즘은 브라우저에서 거름)
        cookie.setPath("/"); // 모든 곳에서 쿠키열람이 가능하도록 설정
        cookie.setMaxAge(60 * 60 * 24); //쿠키 만료시간 설정
        return cookie;
    }
}
