package com.springboot.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller
public class HelloController {
    //@AuthenticationPrincipal 기반으로 가장 많이 쓰임
    @GetMapping("/hello-oauth2")
    public String home(@AuthenticationPrincipal OAuth2User oAuth2User){
//        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
//        System.out.println(oAuth2User);
        System.out.println(oAuth2User.getAttributes().get("email"));
//        OAuth2User oAuth2User = (OAuth2User)SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getPrincipal();
//
//        System.out.println(oAuth2User.getAttributes().get("email"));

        return "hello-oauth2";
    }
}
