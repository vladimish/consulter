package com.vladimish.consulter.frontend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vladimish.consulter.frontend.EnvConfig;
import com.vladimish.consulter.frontend.models.CheckResponse;
import com.vladimish.consulter.frontend.models.SignUpModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class IndexController {
    @GetMapping("/")
    public String signup(Model model, HttpServletRequest request) {
        var cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("auth")) {
                    if (cookies[i].getValue() == null || cookies[i].getValue() == "") {
                        break;
                    }
                    var restTemplate = new RestTemplate();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<byte[]> req = new HttpEntity<byte[]>("{}".getBytes(), headers);
                    try {
                        ResponseEntity<String> resp = restTemplate.postForEntity("http://" + EnvConfig.gatewayURL + "/check?token=" + cookies[i].getValue(), req, String.class);

                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);

                        try {
                            var v = objectMapper.readValue(resp.getBody(), CheckResponse.class);
                            model.addAttribute("username", v.getName());
                            model.addAttribute("privileges", v.getPrivileges());
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "index";
    }
}
