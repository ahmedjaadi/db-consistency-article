package com.consistency.example.consistencydb.controller;

import com.consistency.example.consistencydb.domain.entity.Sale;
import com.consistency.example.consistencydb.domain.dto.SaleStatusCount;
import com.consistency.example.consistencydb.domain.service.SaleService;
import com.consistency.example.consistencydb.domain.service.paginationzero.PaginationZeroService;
import com.consistency.example.consistencydb.domain.service.repeatableread.RepeatableReadService;
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

    @Autowired
    PaginationZeroService paginationZeroService;

    @Autowired
    SaleService saleService;

    @PostMapping("/repeatable-read")
    public ResponseEntity<Void> repeatable() {
        Thread.ofVirtual().start(() -> repeatableReadService.process());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/pagination-zero")
    public ResponseEntity<Void> pagination() {
        Thread.ofVirtual().start(() -> paginationZeroService.process(false));
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/pagination-zero-fixed")
    public ResponseEntity<Void> paginationFixed() {
        Thread.ofVirtual().start(() -> paginationZeroService.process(true));
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/status-count")
    public ResponseEntity<List<SaleStatusCount>> statusCount() {
        var response = saleService.getSalesStatusCount();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/elapsed-time")
    public ResponseEntity<String> elapsedTime() {
        var response = saleService.getTotalTimeElapsed();
        return ResponseEntity.ok("Total time elapsed in seconds: " + response);
    }

    @GetMapping("/sales")
    public ResponseEntity<Page<Sale>> getAllSales(Pageable pageable) {
        var response = saleService.getAllSales(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {
        saleService.updateAllToNotInitialize();
        return ResponseEntity.ok().build();
    }
}
