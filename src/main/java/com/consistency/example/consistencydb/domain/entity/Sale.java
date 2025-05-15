package com.consistency.example.consistencydb.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "sale")
public class Sale {

    public Sale(){
        this.setCreatedAt(Instant.now());
        this.setUpdatedAt(Instant.now());
    }

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SaleStatus status;

    @Column(name = "sold")
    private BigDecimal sold;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public Sale(SaleStatus status, BigDecimal sold) {
        this();
        this.status = status;
        this.sold = sold;
    }

    public Long getId() {
        return id;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public BigDecimal getSold() {
        return sold;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public void setSold(BigDecimal sold) {
        this.sold = sold;
    }

    public Instant getCreatedAt() { return createdAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}

