package com.vladimish.consulter.frontend.gateway;

import com.vladimish.consulter.frontend.EnvConfig;
import com.vladimish.consulter.frontend.models.Meeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
public class Timetable {
    public static Meeting[] getAllTimetables(String token, String type) {
        var restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> req = new HttpEntity<>("{\"type\":\"" + type + "\",\"token\":\"" + token + "\",\"id\":\"" + UUID.randomUUID().toString() + "\"}", headers);
        ResponseEntity<String> resp = restTemplate.postForEntity(EnvConfig.gatewayURL + "/get_timetables", req, String.class);
        log.info(resp.getBody());
        return null;
    }
}
