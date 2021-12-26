package com.vladimish.consulter.frontend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.frontend.EnvConfig;
import com.vladimish.consulter.frontend.gateway.Timetable;
import com.vladimish.consulter.frontend.models.CheckResponse;
import com.vladimish.consulter.frontend.models.Record;
import com.vladimish.consulter.frontend.models.RecordForm;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.awt.print.Book;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@Log4j2
public class UserPanelController {
    @GetMapping("/user_panel")
    public String handleUserPanel(@CookieValue("auth") String token, Model model, HttpServletRequest request) {
        var tts = Timetable.getAllTimetables(token, "client");
        model.addAttribute("tts", tts);
        model.addAttribute("record", new RecordForm());

        var cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("auth")) {
                var restTemplate = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<byte[]> req = new HttpEntity<byte[]>("{}".getBytes(), headers);
                ResponseEntity<String> resp = restTemplate.postForEntity("http://" + EnvConfig.gatewayURL + "/check?token=" + cookies[i].getValue(), req, String.class);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);

                try {
                    var v = objectMapper.readValue(resp.getBody(), CheckResponse.class);
                    model.addAttribute("username", v.getName());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        return "user_panel";
    }

    @PostMapping(value = "/add_record")
    public String handleAddRecord(@ModelAttribute RecordForm r, Model model, HttpServletRequest request) {
        model.addAttribute("record", r);
        var cookies = request.getCookies();
        var restTemplate = new RestTemplate();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("auth")) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<byte[]> req = new HttpEntity<byte[]>("{}".getBytes(), headers);
                ResponseEntity<String> resp = restTemplate.postForEntity(EnvConfig.gatewayURL + "/check?token=" + cookies[i].getValue(), req, String.class);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
                objectMapper.registerModule(new JavaTimeModule());

                try {
                    var v = objectMapper.readValue(resp.getBody(), CheckResponse.class);
                    Record record = new Record();
                    record.setEmployee(null);
                    log.info(LocalDateTime.parse(r.getStart()));
                    record.setStart(LocalDateTime.parse(r.getStart()));
                    record.setTopic(r.getTopic());
                    record.setClient(v.getEmail());
                    HttpHeaders headers2 = new HttpHeaders();
                    headers2.setContentType(MediaType.APPLICATION_JSON);
                    var str = objectMapper.writeValueAsString(record);
                    HttpEntity<byte[]> req2 = new HttpEntity<byte[]>(str.getBytes(), headers2);
                    ResponseEntity<String> resp2 = restTemplate.postForEntity(EnvConfig.gatewayURL + "/add_timetable", req2, String.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return "redirect:/user_panel";
    }
}
