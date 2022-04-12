package com.sagapoc.reservationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sagapoc.reservationservice.model.Reservation;
import com.sagapoc.reservationservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservation")
    public ResponseEntity<Reservation> makeReservation(@RequestBody Reservation request) throws JsonProcessingException {
        Reservation reservation = this.reservationService.registerReservationRequest(request);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return new ResponseEntity<>(this.reservationService.getAllReservations(), HttpStatus.OK);
    }

    @GetMapping("/reservation/{id}")
    public ResponseEntity<Reservation> getAllReservations(@PathVariable Long id) {
        Reservation reservationById = this.reservationService.findReservationById(id);
        return new ResponseEntity<>(reservationById, null == reservationById ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}
