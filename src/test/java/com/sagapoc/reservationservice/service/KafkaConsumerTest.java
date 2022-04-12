package com.sagapoc.reservationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapoc.reservationservice.model.Reservation;
import com.sagapoc.reservationservice.model.StatusEnum;
import com.sagapoc.reservationservice.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @InjectMocks
    private KafkaConsumer underTest;

    @Mock
    private ReservationRepository mockRepository;

    @Test
    void whenReservationStatusIsReservedEntityIsSaved() throws JsonProcessingException {
        Reservation reservation = new Reservation();
        reservation.setStatus(StatusEnum.RESERVED);
        underTest.receive(new ObjectMapper().writeValueAsString(reservation));
        verify(mockRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void whenReservationStatusIsCancelledEntityIsSaved() throws JsonProcessingException {
        Reservation reservation = new Reservation();
        reservation.setStatus(StatusEnum.CANCELED);
        underTest.receive(new ObjectMapper().writeValueAsString(reservation));
        verify(mockRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void whenReservationStatusIsPendingEntityIsNotSaved() throws JsonProcessingException {
        Reservation reservation = new Reservation();
        reservation.setStatus(StatusEnum.PENDING);
        underTest.receive(new ObjectMapper().writeValueAsString(reservation));
        verify(mockRepository, times(0)).save(any(Reservation.class));
    }



}