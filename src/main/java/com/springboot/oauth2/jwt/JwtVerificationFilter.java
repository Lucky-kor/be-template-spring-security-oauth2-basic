package com.springboot.oauth2.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final JWTAuthorityUtils jwtAuthorityUtils;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer, JWTAuthorityUtils jwtAuthorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.jwtAuthorityUtils = jwtAuthorityUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
            if(jwtTokenizer.verifySignature(accessToken, jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()))){
                Map<String, Object> claims =
                        verifyJws(accessToken);
                setAuthenticationToContext(claims);
            }
            else {

            }
        }
        catch (IllegalArgumentException ie){
            request.setAttribute("유효하지 않은 토큰", ie);
        }
        catch (SignatureException se){
            String refreshToken = jwtAuthorityUtils.getRefreshToken(request);
            if(refreshToken != null && jwtTokenizer.verifySignature(refreshToken,
                    jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()))){
                String newAccessToken = jwtTokenizer.reissueAccessToken(refreshToken, jwtAuthorityUtils);

                Map<String, Object> newclaims =
                        verifyJws(newAccessToken);
                response.setHeader("Authorization", "Bearer " + newAccessToken);
                setAuthenticationToContext(newclaims);
            }
            else {
                request.setAttribute("사용자 인증 실패", se);
            }
        }
        catch (ExpiredJwtException ee){
            request.setAttribute("토큰 기한 만료", ee);
        }
        catch (Exception e){
            request.setAttribute("exception", e);
        }

        // 다음 작업을 수행
        filterChain.doFilter(request, response);
    }

    @Override
    protected  boolean shouldNotFilter(HttpServletRequest request){
        // 검증하지 않아도 될 때
        String authorization = request.getHeader("Authorization");
        return authorization == null || !authorization.startsWith("Bearer ");
    }

    private Map<String, Object> verifyJws(String token){
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(token, base64EncodedSecretKey).getBody();
        return claims;
    }

    private  void setAuthenticationToContext(Map<String, Object> claims){
        String username = (String) claims.get("username");
        List<GrantedAuthority> authorityList =
                jwtAuthorityUtils.createAuthorities((List)claims.get("roles"));

        // 씨큐리티 컨텍스트엔 패스워드를 넣지X
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username, null, authorityList
        );
        // 씨큐리티 컨텍스트에 넣기
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
