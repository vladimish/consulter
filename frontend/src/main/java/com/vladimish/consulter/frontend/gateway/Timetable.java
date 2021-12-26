package com.vladimish.consulter.frontend.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.frontend.EnvConfig;
import com.vladimish.consulter.frontend.models.MeetingsResponse;
import com.vladimish.consulter.frontend.models.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
public class Timetable {
    public static Record[] getAllTimetables(String token, String type) {
        var restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> req = new HttpEntity<>("{\"type\":\"" + type + "\",\"token\":\"" + token + "\",\"id\":\"" + UUID.randomUUID().toString() + "\"}", headers);
        ResponseEntity<String> resp = restTemplate.postForEntity("http://" + EnvConfig.gatewayURL + "/get_timetables", req, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        MeetingsResponse response = new MeetingsResponse();
        try {
            response = objectMapper.readValue(resp.getBody(), MeetingsResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response.getRecords();
    }
}
