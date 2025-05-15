package com.consistency.example.consistencydb.domain.service;

import com.consistency.example.consistencydb.domain.dto.SaleStatusCount;
import com.consistency.example.consistencydb.domain.entity.Sale;
import com.consistency.example.consistencydb.domain.entity.SaleAudit;
import com.consistency.example.consistencydb.domain.entity.SaleStatus;
import com.consistency.example.consistencydb.repository.SaleAuditRepository;
import com.consistency.example.consistencydb.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleAuditRepository saleAuditRepository;

    @Transactional
    public void updateSaleStatus(Long id, SaleStatus status) {
        Sale sale = saleRepository.findById(id).orElseThrow();
        var rows = saleRepository.updateStatusById(id, status, sale.getVersion());

        if (rows == 0) return;

        var salePersisted = saleRepository.findById(id);
        var saleAudit = SaleAudit.fromSale(salePersisted.get());
        saleAuditRepository.save(saleAudit);
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

    public void processSaleEvent(Long id, Boolean idempotency) {
        if(idempotency) {
            var sale = this.findById(id);
            if (sale.get().getStatus().equals(SaleStatus.PROCESSING))
                return;
        }

        this.updateSaleStatus(id, SaleStatus.PROCESSING);
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
