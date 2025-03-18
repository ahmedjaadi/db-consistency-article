package com.consistency.example.consistencydb.domain.service;

import com.consistency.example.consistencydb.domain.dto.SaleStatusCount;
import com.consistency.example.consistencydb.domain.entity.Sale;
import com.consistency.example.consistencydb.domain.entity.SaleStatus;
import com.consistency.example.consistencydb.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    @Autowired
    private SaleRepository saleRepository;

    public void updateSaleStatus(Long id, SaleStatus status) {
        saleRepository.updateStatusById(id, status);
    }

    public List<SaleStatusCount> getSalesStatusCount(){
        return saleRepository.countByEachStatus();
    }

    public Page<Sale> getAllSales(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }

    public Page<Sale> findAllByStatus(SaleStatus status, Pageable pageable) {
        return saleRepository.findAllByStatus(status, pageable);
    }

    public Optional<Sale> findById(Long id) {
        return saleRepository.findById(id);
    }

    public void updateAllToNotInitialize() {
        saleRepository.updateStatusForAll(SaleStatus.NOT_INITIALIZED);
    }

    public void updateAllCreatedAt() {
        saleRepository.updateCreatedAtForAll();
    }

    public Long getTotalTimeElapsed() {
        return saleRepository.getTotalElapsedTimeInSeconds();
    }
}
