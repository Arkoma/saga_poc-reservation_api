package com.sagapoc.reservationservice.service;

import com.sagapoc.reservationservice.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, Reservation payload) {
        System.out.println("sending payload='" + payload.toString() + "' to topic='" + topic + "'");
        kafkaTemplate.send(topic, payload.toString());
    }
}
