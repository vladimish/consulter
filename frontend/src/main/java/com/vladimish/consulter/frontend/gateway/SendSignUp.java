package com.vladimish.consulter.frontend.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vladimish.consulter.frontend.EnvConfig;
import com.vladimish.consulter.frontend.models.SignUpModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Slf4j
public class SendSignUp {
    public static void sendSignUp(SignUpModel model) {
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

        ResponseEntity<String> resp = restTemplate.postForEntity(EnvConfig.gatewayURL + "/register", req, String.class);
    }
}