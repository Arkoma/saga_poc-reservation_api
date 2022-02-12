package com.sagapoc.reservationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapoc.reservationservice.model.Reservation;
import com.sagapoc.reservationservice.model.StatusEnum;
import com.sagapoc.reservationservice.repository.ReservationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private String payload = null;

    private final ReservationRepository repository;

    public KafkaConsumer(ReservationRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void receive(String reservation) {
        System.out.println("received payload='" + reservation + "'");
        setPayload(reservation);
        try {
            Reservation payloadReservation = new ObjectMapper().readValue(this.payload, Reservation.class);
            if (StatusEnum.PENDING != payloadReservation.getStatus()) {
                this.repository.save(payloadReservation);
            }
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
}
