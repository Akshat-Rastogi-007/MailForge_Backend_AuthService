package com.rastogi.mailforge.AuthService.service.kafka;

import com.rastogi.mailforge.AuthService.dto.kafka.UserRegistrationEvent;
import com.rastogi.mailforge.AuthService.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Kafka producer service for Auth Service events
 * Publishes user registration events for downstream services to consume
 */
@Service
@Slf4j
public class UserRegistrationEventPublisher {
    
    private final KafkaTemplate<String, UserRegistrationEvent> kafkaTemplate;
    
    @Value("${kafka.topic.name}")
    private String topicName;
    
    public UserRegistrationEventPublisher(KafkaTemplate<String, UserRegistrationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    /**
     * Publish a user registration event to Kafka
     * This should be called after a user successfully registers
     * 
     * @param user The User entity that was registered
     */
    public void publishUserRegistrationEvent(User user) {
        try {
            UserRegistrationEvent event = UserRegistrationEvent.builder()
                    .userId(user.getId())
                    .username(user.getUsername())
                    .externalMail(user.getExternalMail())
                    .status(user.getStatus() != null ? user.getStatus().toString() : "ACTIVE")
                    .createdAt(convertToMillis(user.getCreatedAt()))
                    .build();
            
            log.info("Publishing user registration event for username: {} to topic: {}", user.getUsername(), topicName);
            
            kafkaTemplate.send(topicName, user.getId(), event)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to publish user registration event for username: {}", user.getUsername(), ex);
                        } else {
                            log.info("Successfully published user registration event for username: {} to partition: {}",
                                    user.getUsername(), result.getRecordMetadata().partition());
                        }
                    });
            
        } catch (Exception e) {
            log.error("Error preparing user registration event for username: {}", user.getUsername(), e);
        }
    }
    
    /**
     * Convert LocalDateTime to milliseconds since epoch
     */
    private Long convertToMillis(LocalDateTime dateTime) {
        if (dateTime == null) {
            return System.currentTimeMillis();
        }
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
