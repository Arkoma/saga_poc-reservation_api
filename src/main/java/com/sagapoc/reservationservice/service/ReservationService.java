//package com.sagapoc.reservationservice.service;
//
//import com.sagapoc.reservationservice.model.Reservation;
//import com.sagapoc.reservationservice.repository.ReservationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ReservationService {
//
//    private final ReservationRepository repository;
//    private final KafkaProducer producer;
//
//    public ReservationService(ReservationRepository repository, KafkaProducer producer) {
//        this.repository = repository;
//        this.producer = producer;
//    }
//
//    public Reservation registerReservationRequest(Reservation reservation) {
//
//        reservation = this.repository.save(reservation);
//    }
//}
