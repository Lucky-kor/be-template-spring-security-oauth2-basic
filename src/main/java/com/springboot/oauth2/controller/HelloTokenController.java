package com.springboot.oauth2.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloTokenController {
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    public HelloTokenController(OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
    }

    @GetMapping("/token-oauth2")
    public String home(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient oAuth2AuthorizedClient){
        OAuth2AccessToken accessToken = oAuth2AuthorizedClient.getAccessToken();
        System.out.println(accessToken.getTokenValue());
        System.out.println(accessToken.getTokenType());
        System.out.println(accessToken.getTokenType().getValue());
        System.out.println(accessToken.getIssuedAt());
        System.out.println(accessToken.getExpiresAt());

        return "hello-oauth2";
    }
}
