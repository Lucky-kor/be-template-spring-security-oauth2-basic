package com.springboot.oauth2.config;

import com.springboot.member.service.MemberService;
import com.springboot.oauth2.jwt.JWTAuthorityUtils;
import com.springboot.oauth2.jwt.JwtTokenizer;
import com.springboot.oauth2.jwt.JwtVerificationFilter;
import com.springboot.oauth2.jwt.Oauth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfigration {
    private final JwtTokenizer jwtTokenizer;
    private final MemberService memberService;
    private final JWTAuthorityUtils jwtAuthorityUtils;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfigration(JwtTokenizer jwtTokenizer, MemberService memberService, JWTAuthorityUtils jwtAuthorityUtils, PasswordEncoder passwordEncoder) {
        this.jwtTokenizer = jwtTokenizer;
        this.memberService = memberService;
        this.jwtAuthorityUtils = jwtAuthorityUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                //CorsConfigurationSource를 만들어줘야함
                .cors(Customizer.withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomFilterConfigure())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        //- 등록된 답변의 내용은 답변을 등록한 관리자만 수정할 수 있어야 한다
                        .antMatchers(HttpMethod.PATCH, "/*/board/qna/reply/*").hasRole("ADMIN")
                        //- 답변은 관리자만 등록할 수 있다.
                        .antMatchers(HttpMethod.POST, "/*/board/qna/reply/*").hasRole("ADMIN")
                        //- 1건의 질문 삭제는 질문을 등록한 회원만 가능하다.
                        .antMatchers(HttpMethod.DELETE, "/*/board/qna/**").hasRole("USER")
                        //- Q&A 게시판은 회원 전용 게시판이다. 따라서 회원으로 등록한 회원만 해당 Q&A 게시판 기능을 이용할 수 있다.
                        .antMatchers(HttpMethod.POST, "/*/board/qna/*").hasRole("USER")
                        .antMatchers(HttpMethod.PATCH, "/*/board/qna/*").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/*/board/qna/*").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/*/board/qna").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/*/board/qna/*").hasRole("USER")
                        //- 등록된 질문의 제목과 내용은 질문을 등록한 회원(고객)만 수정할 수 있어야 한다.
                        .antMatchers(HttpMethod.PATCH, "/*/board/qna/*").hasRole("USER")
                        //- 비밀글 상태인 질문은 질문을 등록한 회원(고객)과 관리자만 조회할 수 있다. (ㅇ)
                        .antMatchers(HttpMethod.POST, "/*/coffees").hasRole("ADMIN")
                        .anyRequest().permitAll())
                //.oauth2Login(Customizer.withDefaults());
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new Oauth2AuthenticationSuccessHandler(
                                jwtTokenizer, jwtAuthorityUtils, memberService
                        )
                        ));
        return http.build();
    }

    public class CustomFilterConfigure extends AbstractHttpConfigurer<CustomFilterConfigure, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception{
            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, jwtAuthorityUtils);

        builder.addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);
        }
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
