package com.sagapoc.reservationservice.service;

import com.sagapoc.reservationservice.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceIT {

    private final ApplicationContext applicationContext;
    private final ReservationRepository repository;
    private final KafkaProducer producer;
    private final ReservationService underTest;

    @Autowired
    public ReservationServiceIT(ApplicationContext applicationContext,
                                ReservationRepository repository,
                                KafkaProducer kafkaProducer,
                                ReservationService reservationService) {
        this.applicationContext = applicationContext;
        this.repository = repository;
        this.producer = kafkaProducer;
        this.underTest = reservationService;

    }

    @Value(value = "${spring.kafka.template.default-topic}")
    private String topic;

    @Test
    void reservationServiceIsABean() {
        assertTrue(applicationContext.containsBean("reservationService"));
    }

    @Test
    void topicIsSetFromSpringConfiguration() {
        String embeddedTopic = Objects.requireNonNull(ReflectionTestUtils.getField(underTest, "topic")).toString();
        assertEquals(topic, embeddedTopic);
    }

    @Test
    void reservationServiceHasInjectedRepositoryAndKafkaProducer() {
        ReservationRepository injectedRepository = (ReservationRepository) ReflectionTestUtils.getField(underTest, "repository");
        KafkaProducer injectedProducer = (KafkaProducer) ReflectionTestUtils.getField(underTest, "producer");
        assertAll(() -> {
            assertSame(repository, injectedRepository);
            assertSame(producer, injectedProducer);
        });
    }
}