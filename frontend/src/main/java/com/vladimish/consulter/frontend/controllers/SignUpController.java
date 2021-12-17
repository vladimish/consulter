package com.vladimish.consulter.frontend.controllers;

import com.vladimish.consulter.frontend.gateway.SendSignUp;
import com.vladimish.consulter.frontend.models.SignUpModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Log4j2
public class SignUpController {

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signup", new SignUpModel());
        return "signup";
    }

    // Handle form submission.
    @PostMapping("/signup")
    public String signupForm(@ModelAttribute SignUpModel signup, Model model) {
        model.addAttribute("signup", signup);
        var res = signup.validate();
        if (res == null) {
            return SendSignUp.sendSignUp(signup);
        } else {
            return "signup";
        }
    }
}
