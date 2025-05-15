package com.consistency.example.consistencydb.domain.service;

import com.consistency.example.consistencydb.domain.entity.SaleAudit;
import com.consistency.example.consistencydb.repository.SaleAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleAuditService {

    @Autowired
    SaleAuditRepository saleAuditRepository;

    public List<SaleAudit> findAll() {
        return saleAuditRepository.findAll();
    }
}
