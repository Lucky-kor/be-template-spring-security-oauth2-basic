package com.springboot.oauth2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloOAuth2Controller {
    @GetMapping("/hello-oauth2")
    public String home(){
        return "hello-oauth2";
    }
}
