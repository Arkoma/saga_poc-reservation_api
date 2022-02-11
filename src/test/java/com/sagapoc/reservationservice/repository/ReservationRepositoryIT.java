package com.sagapoc.reservationservice.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapoc.reservationservice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationRepositoryIT {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ReservationRepository underTest;

    @BeforeEach
    void setup() {
        underTest.deleteAll();
    }

    @Test
    void reservationRepositoryIsABean() {
        assertTrue(applicationContext.containsBean("reservationRepository"));
    }

    @Test
    void testSaveReservation() throws JsonProcessingException {
        final String hotelName = "Holiday Inn";
        final String carMake = "Ford";
        final String carModel = "Model-T";
        final String flightNumber = "801";
        final String customerName = "Tom Brady";
        Reservation reservation = new Reservation();
        reservation.setCustomerName(customerName);
        reservation.setHotelName(hotelName);
        reservation.setCarMake(carMake);
        reservation.setCarModel(carModel);
        reservation.setStatus(StatusEnum.PENDING);
        String json = new ObjectMapper().writeValueAsString(reservation);
        Reservation savedReservation = underTest.save(reservation);
        Reservation retrievedReservationFromDB = underTest.findById(savedReservation.getId()).orElse(null);
        assertAll(() -> {
            assertEquals(reservation.getCustomerName(), retrievedReservationFromDB.getCustomerName());
            assertEquals(reservation.getHotelName(), retrievedReservationFromDB.getHotelName());
            assertEquals(reservation.getCarMake(), retrievedReservationFromDB.getCarMake());
            assertEquals(reservation.getCarModel(), retrievedReservationFromDB.getCarModel());
            assertEquals(reservation.getFlightNumber(), retrievedReservationFromDB.getFlightNumber());
            assertEquals(reservation.getStatus(), retrievedReservationFromDB.getStatus());
        } );
    }

}