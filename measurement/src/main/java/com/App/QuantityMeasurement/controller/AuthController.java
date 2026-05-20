package com.App.QuantityMeasurement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class AuthController {

    @GetMapping("/api/auth/login")
    public String login() {
        return "redirect:/oauth2/authorization/google";
    }


    @GetMapping("/api/auth/me")
    @ResponseBody
    public Map<String, Object> currentUser(
            @AuthenticationPrincipal OAuth2User user) {

        if (user == null) {
            return Map.of(
                    "authenticated", false
            );
        }

        return Map.of(
                "authenticated", true,
                "email", user.getAttribute("email"),
                "name", user.getAttribute("name"),
                "picture", user.getAttribute("picture")
        );
    }
}