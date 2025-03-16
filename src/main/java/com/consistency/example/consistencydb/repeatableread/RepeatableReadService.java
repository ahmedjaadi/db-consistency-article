package com.consistency.example.consistencydb.repeatableread;

import com.consistency.example.consistencydb.domain.Sale;
import com.consistency.example.consistencydb.domain.SaleStatus;
import com.consistency.example.consistencydb.domain.dto.SaleStatusCount;
import com.consistency.example.consistencydb.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class RepeatableReadService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private RepeatableReadProducer repeatableReadProducer;

    private Long elapsedTime = 0L;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void process() {
        Pageable pagination = PageRequest.of(0, 100);
        Page<Sale> pageSale;

        var startAt = Instant.now();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            do {
                pageSale = saleRepository.findAllByStatus(SaleStatus.NOT_INITIALIZED, pagination);

                var futures = pageSale.stream().map(sale ->
                        CompletableFuture.runAsync(
                                () -> repeatableReadProducer.sendMessage("my-topic", "key", sale.getId().toString()),
                                executor
                        )
                ).toArray(CompletableFuture[]::new);

                CompletableFuture.allOf(futures).join();

                pagination = pageSale.getPageable().next();
            } while (pageSale.hasNext());
        }

        Duration interval = Duration.between(startAt, Instant.now());
        elapsedTime = interval.getSeconds();
    }

    public void updateSaleStatus(Long id, SaleStatus status) {
        saleRepository.updateStatusById(id, status);
    }

    public List<SaleStatusCount> getSalesStatusCount(){
        return saleRepository.countByEachStatus();
    }

    public Page<Sale> getAllSales(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }
}
