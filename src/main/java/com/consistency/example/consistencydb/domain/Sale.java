package com.consistency.example.consistencydb.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sale")
public class Sale {

    public Sale(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SaleStatus status;

    @Column(name = "sold")
    private BigDecimal sold;

    public Sale(SaleStatus status, BigDecimal sold) {
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
}

