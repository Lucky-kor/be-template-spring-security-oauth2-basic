package com.springboot.oauth2.jwt;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class Oauth2AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {
}
