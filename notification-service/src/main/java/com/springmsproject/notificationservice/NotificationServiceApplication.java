package com.springmsproject.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics="notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
            //send out email notification but as we are not having such thing we ae just logging
        log.info("Received notification for the order -{}",orderPlacedEvent.getOrderNumber());
    }

}