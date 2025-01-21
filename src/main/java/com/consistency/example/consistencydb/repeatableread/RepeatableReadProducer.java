package com.consistency.example.consistencydb.repeatableread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RepeatableReadProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        System.err.println("Failed to send message: " + exception.getMessage());
                    } else {
                        System.out.println("Message sent: " + message);
                    }
                });
    }
}
