package com.sagapoc.reservationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagapoc.reservationservice.model.Reservation;
import com.sagapoc.reservationservice.model.StatusEnum;
import com.sagapoc.reservationservice.service.KafkaConsumer;
import com.sagapoc.reservationservice.service.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class EmbeddedKafkaIT {

    @Autowired
    private KafkaProducer producer;

    @Autowired
    private KafkaConsumer consumer;

    @Value("${spring.kafka.template.default-topic}")
    private String topic;

//    @Test
    public void givenEmbeddedKafkaBroker_whenSendingtoSimpleProducer_thenMessageReceived()
            throws Exception {
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
        producer.send(topic, reservation);
//        consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);

        assertThat(consumer.getLatch().getCount(), equalTo(0L));
        String json = consumer.getPayload();
        Reservation received = new ObjectMapper().readValue(json, Reservation.class);
        assertEquals(reservation.getCustomerName(), received.getCustomerName());
    }
}
