package com.sagapoc.reservationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sagapoc.reservationservice.model.Reservation;
import com.sagapoc.reservationservice.model.StatusEnum;
import com.sagapoc.reservationservice.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Value(value = "${spring.kafka.template.default-topic}")
    private String topic;

    private final ReservationRepository repository;
    private final KafkaProducer producer;

    public ReservationService(ReservationRepository repository, KafkaProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    public Reservation registerReservationRequest(Reservation reservation) throws JsonProcessingException {
        reservation = this.repository.save(reservation);
        reservation.setStatus(StatusEnum.PENDING);
        this.producer.send(topic, reservation);
        return reservation;
    }

    public List<Reservation> getAllReservations() {
        return this.repository.findAll();
    }

    public Reservation findReservationById(Long id) {
        return this.repository.findById(id).orElse(null);
    }
}
