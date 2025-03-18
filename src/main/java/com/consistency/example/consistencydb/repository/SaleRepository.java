package com.consistency.example.consistencydb.repository;

import com.consistency.example.consistencydb.domain.entity.Sale;
import com.consistency.example.consistencydb.domain.entity.SaleStatus;
import com.consistency.example.consistencydb.domain.dto.SaleStatusCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Page<Sale> findAllByStatus(SaleStatus status, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Sale s SET s.status = :status, s.updatedAt = CURRENT_TIMESTAMP WHERE s.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") SaleStatus status);

    @Query("SELECT new com.consistency.example.consistencydb.domain.dto.SaleStatusCount(s.status, COUNT(s)) FROM Sale s GROUP BY s.status")
    List<SaleStatusCount> countByEachStatus();

    @Modifying
    @Transactional
    @Query("UPDATE Sale s SET s.status = :status")
    void updateStatusForAll(@Param("status") SaleStatus saleStatus);

    @Modifying
    @Transactional
    @Query("UPDATE Sale s SET s.createdAt = CURRENT_TIMESTAMP")
    void updateCreatedAtForAll();

    @Query(value = """
    SELECT EXTRACT(EPOCH FROM (MAX(s.updated_at) - MIN(s.created_at)))
    FROM Sale s
    """, nativeQuery = true)
    Long getTotalElapsedTimeInSeconds();
}
