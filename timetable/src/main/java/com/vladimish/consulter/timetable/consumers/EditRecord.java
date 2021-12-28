package com.vladimish.consulter.timetable.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.timetable.ConfigureRabbitMQ;
import com.vladimish.consulter.timetable.db.Record;
import com.vladimish.consulter.timetable.db.RecordRepository;
import com.vladimish.consulter.timetable.models.AddRecordRequest;
import com.vladimish.consulter.timetable.models.AddRecordResponse;
import com.vladimish.consulter.timetable.models.EditRecordRequest;
import com.vladimish.consulter.timetable.producers.AddRecordProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EditRecord {

    @Autowired
    RecordRepository recordRepository;

    @RabbitListener(queues = ConfigureRabbitMQ.PRODUCER_TIMETABLE_EDIT_QUEUE_NAME)
    public void handleRegisterReply(String messageBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);

        log.info("Handling: " + messageBody);

        EditRecordRequest req = new EditRecordRequest();
        try {
            req = objectMapper.readValue(messageBody, EditRecordRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        var rec = recordRepository.findAllByClient(req.getClient());
        if (rec.size() > 0) {
            rec.get(0).setEmployee(req.getEmployee());
            recordRepository.save(rec.get(0));
        }

        AddRecordResponse response = new AddRecordResponse();
        response.setConsumer(req.getConsumer());
        response.setStatus("OK");

        AddRecordProducer.sendResponse(response);

    }

}
