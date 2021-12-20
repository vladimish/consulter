package com.vladimish.consulter.frontend.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vladimish.consulter.frontend.EnvConfig;
import com.vladimish.consulter.frontend.models.LoginModel;
import com.vladimish.consulter.frontend.models.LoginResponse;
import com.vladimish.consulter.frontend.models.SignUpModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
public class SendLogin {
    public static String sendLogin(LoginModel model, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        var str = new byte[]{};
        try {
            str = objectMapper.writeValueAsBytes(model);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        var restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<byte[]> req = new HttpEntity<byte[]>(str, headers);

        log.info(new String(str, StandardCharsets.UTF_8));

        ResponseEntity<String> resp = restTemplate.postForEntity(EnvConfig.gatewayURL + "/login", req, String.class);
        log.info(String.valueOf(resp.getStatusCode()));

        log.info(resp.getBody());
        LoginResponse res = new LoginResponse();
        try {
            res = objectMapper.readValue(resp.getBody(), LoginResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        var cookie = new Cookie("auth", res.getToken());
        cookie.setMaxAge(604800);
        response.addCookie(cookie);

        if (resp.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            return "500";
        }
        return "redirect:/user_panel";
    }
}
