package com.sagapoc.reservationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sagapoc.reservationservice.model.Reservation;
import com.sagapoc.reservationservice.model.StatusEnum;
import com.sagapoc.reservationservice.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService underTest;

    @Mock
    private ReservationRepository mockRepository;

    @Mock
    private KafkaProducer mockProducer;

    @Test
    void registerReservationRequestSavesReservationToRepository() throws JsonProcessingException {
        Reservation reservation = new Reservation();
        when(mockRepository.save(any(Reservation.class))).thenReturn(reservation);
        underTest.registerReservationRequest(reservation);
        verify(mockRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void returnedReservationIsMarkedAsPending() throws JsonProcessingException {
        Reservation reservation = new Reservation();
        when(mockRepository.save(any(Reservation.class))).thenReturn(reservation);
        Reservation result = underTest.registerReservationRequest(reservation);
        assertEquals(StatusEnum.PENDING, result.getStatus());
    }

    @Test
    void registerReservationRequestSendsReservationOntoTheQueue() throws JsonProcessingException {
        Reservation reservation = new Reservation();
        when(mockRepository.save(any(Reservation.class))).thenReturn(reservation);
        underTest.registerReservationRequest(reservation);
        verify(mockProducer, times(1)).send(any(), eq(reservation));
    }

    @Test
    void getAllReservationsCallsRepositoryFindAll() {
        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        when(mockRepository.findAll()).thenReturn(reservations);
        List<Reservation> results = underTest.getAllReservations();
        assertAll(() -> {
            verify(mockRepository, times(1)).findAll();
            assertEquals(2, results.size());
        });

    }

    @Test
    void getReservationByIdCallsRepositoryFindById() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Reservation result = underTest.findReservationById(reservation.getId());
        assertAll(() -> {
            verify(mockRepository, times(1)).findById(1L);
            assertEquals(reservation.getId(), result.getId());
        });
    }
}