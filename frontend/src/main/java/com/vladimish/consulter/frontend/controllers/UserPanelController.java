package com.vladimish.consulter.frontend.controllers;

import com.vladimish.consulter.frontend.gateway.Timetable;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.awt.print.Book;

@Controller
@Log4j2
public class UserPanelController {
    @GetMapping("/user_panel")
    public String handleUserPanel(@CookieValue("auth") String token, Model model){
        var tts = Timetable.getAllTimetables(token, "client");
        model.addAttribute("tts", tts);

        return "user_panel";
    }
}
