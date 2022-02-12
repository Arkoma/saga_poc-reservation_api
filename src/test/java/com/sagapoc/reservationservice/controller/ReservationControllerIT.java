package com.sagapoc.reservationservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapoc.reservationservice.model.Reservation;
import com.sagapoc.reservationservice.model.StatusEnum;
import com.sagapoc.reservationservice.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ReservationControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ReservationRepository repository;

    private MockMvc mockMvc;
    private Reservation reservation;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        reservation = new Reservation();
        reservation.setCustomerName("customer");
        reservation.setHotelName("hotel");
        reservation.setCarMake("make");
        reservation.setCarModel("model");
        reservation.setFlightNumber("801");
        repository.deleteAll();
    }

    private MvcResult registerReservation() throws Exception {
        String json = mapper.writeValueAsString(this.reservation);
        return this.mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();
    }

    @Test
    @Transactional
    void makeReservationSavesReservation() throws Exception {
        MvcResult mvcResult = registerReservation();
        String response = mvcResult.getResponse().getContentAsString();
        Reservation result = mapper.readValue(response, Reservation.class);
        Reservation actualEntity = this.repository.getById(result.getId());
        assertAll(() -> {
            assertEquals(StatusEnum.PENDING ,actualEntity.getStatus());
            assertEquals(result.getId(), actualEntity.getId());
            assertEquals(result.getFlightNumber(), actualEntity.getFlightNumber());
        });
    }

    @Test
    @Transactional
    void getAllReservations() throws Exception {
        registerReservation();
        registerReservation();
        MvcResult foundResult = this.mockMvc.perform(get("/reservations"))
                .andExpectAll(
                    status().isOk(),
                    content().contentType(MediaType.APPLICATION_JSON)
                ).andReturn();
        String foundResponseJson = foundResult.getResponse().getContentAsString();
        List<Reservation> foundReservations = mapper.readValue(foundResponseJson, new TypeReference<>(){});
        assertEquals(2, foundReservations.size());
    }

    @Test
    @Transactional
    void getReservationByIdReturnsReservation() throws Exception {
        MvcResult mvcResult = registerReservation();
        String response = mvcResult.getResponse().getContentAsString();
        Reservation result = mapper.readValue(response, Reservation.class);
        MvcResult foundMvcResult = this.mockMvc.perform(get("/reservation/" + result.getId()))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                ).andReturn();
        String foundResponse = foundMvcResult.getResponse().getContentAsString();
        Reservation foundResult = mapper.readValue(foundResponse, Reservation.class);
        Reservation actualEntity = this.repository.getById(result.getId());
        assertAll(() -> {
            assertEquals(foundResult.getStatus(), actualEntity.getStatus());
            assertEquals(foundResult.getId(), actualEntity.getId());
            assertEquals(foundResult.getFlightNumber(), actualEntity.getFlightNumber());
        });
    }
}