package com.vladimish.consulter.frontend.controllers;

import com.vladimish.consulter.frontend.gateway.SendLogin;
import com.vladimish.consulter.frontend.gateway.SendSignUp;
import com.vladimish.consulter.frontend.models.LoginModel;
import com.vladimish.consulter.frontend.models.SignUpModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String signup(Model model) {
        model.addAttribute("login", new SignUpModel());
        return "login";
    }

    // Handle form submission.
    @PostMapping("/login")
    public String signupForm(@ModelAttribute LoginModel login, Model model) {
        model.addAttribute("login", login);
        var res = login.validate();
        if (res == null) {
            return SendLogin.sendLogin(login);
        } else {
            return "login";
        }
    }
}
