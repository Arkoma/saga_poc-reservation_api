package com.sagapoc.reservationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sagapoc.reservationservice.model.Reservation;
import com.sagapoc.reservationservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservationController {

    private final ReservationService ReservationService;

    public ReservationController(ReservationService ReservationService) {
        this.ReservationService = ReservationService;
    }


    @PostMapping("/reservation")
    public ResponseEntity<Reservation> makeReservation(@RequestBody Reservation request) throws JsonProcessingException {
        Reservation Reservation = this.ReservationService.registerReservationRequest(request);
        return new ResponseEntity<>(Reservation, HttpStatus.CREATED);
    }
}
