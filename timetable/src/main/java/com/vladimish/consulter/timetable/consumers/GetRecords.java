package com.vladimish.consulter.timetable.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.timetable.ConfigureRabbitMQ;
import com.vladimish.consulter.timetable.db.Record;
import com.vladimish.consulter.timetable.db.RecordRepository;
import com.vladimish.consulter.timetable.models.GetRecordsRequest;
import com.vladimish.consulter.timetable.models.GetRecordsResponse;
import com.vladimish.consulter.timetable.models.RecordResponse;
import com.vladimish.consulter.timetable.producers.GetRecordsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GetRecords {

    @Autowired
    RecordRepository recordRepository;

    @RabbitListener(queues = ConfigureRabbitMQ.CONSUMER_TIMETABLE_GET_QUEUE_NAME)
    public void handleRegisterReply(String messageBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);

        GetRecordsRequest req = new GetRecordsRequest();
        try {
            req = objectMapper.readValue(messageBody, GetRecordsRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<Record> recs = new ArrayList<Record>();

        if (req.getType().equals("client")) {
            recs = recordRepository.findAllByClient(req.getEmail());
        } else {
            recs = recordRepository.findAllByEmployee(req.getEmail());
        }

        GetRecordsResponse response = new GetRecordsResponse();
        response.setConsumer(req.getConsumer());

        var res = new RecordResponse[recs.size()];
        for (int i = 0; i < recs.size(); i++) {
            res[i] = new RecordResponse();
            res[i].setClient(recs.get(i).getClient());
            res[i].setEmployee(recs.get(i).getEmployee());
            res[i].setStart(recs.get(i).getStart());
            res[i].setTopic(recs.get(i).getTopic());
        }

        response.setId(req.getId());
        response.setRecords(res);

        try {
            log.info("Res is ready: " + objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        GetRecordsProducer.sendResponse(response);

    }

}
