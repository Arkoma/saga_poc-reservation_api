package com.sagapoc.reservationservice.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class KafkaConsumer {

    private String payload = null;
    private CountDownLatch latch = new CountDownLatch(0);

    @KafkaListener(topics = "${kafka.topicName}")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("received payload='" + consumerRecord.toString() + "'");
        setPayload(consumerRecord.toString());
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
}
