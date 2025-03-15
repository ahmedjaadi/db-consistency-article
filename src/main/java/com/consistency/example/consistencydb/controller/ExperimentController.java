package com.consistency.example.consistencydb.controller;

import com.consistency.example.consistencydb.domain.Sale;
import com.consistency.example.consistencydb.domain.SaleStatus;
import com.consistency.example.consistencydb.repeatableread.RepeatableReadProducer;
import com.consistency.example.consistencydb.repository.SaleRepository;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
public class ExperimentController {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private RepeatableReadProducer repeatableReadProducer;

    @GetMapping("/send")
    public String sendMessage() {
        repeatableReadProducer.sendMessage("my-topic", "key", "test");
        return "Message sent!";
    }

    @GetMapping("/sale")
    public String sale() {
        Sale sale = new Sale(SaleStatus.NOT_INITIALIZED, BigDecimal.ONE);
        sale = saleRepository.save(sale);
        Optional<Sale> result = saleRepository.findById(sale.getId());
        System.out.println(result.get().getStatus());
        return "Message sent!";
    }
}
