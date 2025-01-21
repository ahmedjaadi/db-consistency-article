package com.consistency.example.consistencydb.controller;

import com.consistency.example.consistencydb.repeatableread.RepeatableReadProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExperimentController {

    @Autowired
    private RepeatableReadProducer repeatableReadProducer;

    @GetMapping("/send")
    public String sendMessage() {
        repeatableReadProducer.sendMessage("my-topic", "key", "test");
        return "Message sent!";
    }
}
