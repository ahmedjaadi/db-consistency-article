package com.consistency.example.consistencydb.controller;

import com.consistency.example.consistencydb.domain.Sale;
import com.consistency.example.consistencydb.domain.SaleStatus;
import com.consistency.example.consistencydb.repeatableread.RepeatableReadProducer;
import com.consistency.example.consistencydb.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<Sale> getAllSales(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }
}
