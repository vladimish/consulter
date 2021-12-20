package com.vladimish.consulter.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {
    @GetMapping("/logout")
    public String resetAuth(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth", null);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
