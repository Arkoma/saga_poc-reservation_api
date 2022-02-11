package com.sagapoc.reservationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapoc.reservationservice.model.Reservation;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, Reservation payload) throws JsonProcessingException {
        System.out.println("sending payload='" + payload.toString() + "' to topic='" + topic + "'");
        String json = new ObjectMapper().writeValueAsString(payload);
        kafkaTemplate.send(topic, json);
    }
}
