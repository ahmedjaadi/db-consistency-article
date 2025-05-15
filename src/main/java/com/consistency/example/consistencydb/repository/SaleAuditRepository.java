package com.consistency.example.consistencydb.repository;

import com.consistency.example.consistencydb.domain.entity.SaleAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleAuditRepository extends JpaRepository<SaleAudit, Long> {
}
