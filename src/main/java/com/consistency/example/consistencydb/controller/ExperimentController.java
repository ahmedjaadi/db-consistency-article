package com.consistency.example.consistencydb.controller;

import com.consistency.example.consistencydb.domain.Sale;
import com.consistency.example.consistencydb.domain.dto.SaleStatusCount;
import com.consistency.example.consistencydb.repeatableread.RepeatableReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExperimentController {
    @Autowired
    RepeatableReadService repeatableReadService;

    @PostMapping("/repeatable-read")
    public ResponseEntity<Void> sendMessage() {
        Thread.ofVirtual().start(() -> repeatableReadService.process());
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/repeatable-read/status-count")
    public ResponseEntity<List<SaleStatusCount>> statusCount() {
        var response = repeatableReadService.getSalesStatusCount();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/repeatable-read")
    public ResponseEntity<Page<Sale>> getAllSales(Pageable pageable) {
        var response = repeatableReadService.getAllSales(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/elapsed")
    public ResponseEntity<String> elapsed() {
        var response = repeatableReadService.getElapsedTime();
        return ResponseEntity.ok("Elapsed time: " + response + " seconds");
    }
}
