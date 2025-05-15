package com.consistency.example.consistencydb.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "sale_audit")
public class SaleAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_audit")
    private Long idAudit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_audit")
    private SaleStatus statusAudit;

    @Column(name = "sold_audit")
    private BigDecimal soldAudit;

    @Column(name = "created_at_audit")
    private Instant createdAtAudit;

    @Column(name = "updated_at_audit")
    private Instant updatedAtAudit;

    public Long getId() {
        return id;
    }

    public Long getIdAudit() {
        return idAudit;
    }

    public void setIdAudit(Long idAudit) {
        this.idAudit = idAudit;
    }

    public SaleStatus getStatusAudit() {
        return statusAudit;
    }

    public void setStatusAudit(SaleStatus statusAudit) {
        this.statusAudit = statusAudit;
    }

    public BigDecimal getSoldAudit() {
        return soldAudit;
    }

    public void setSoldAudit(BigDecimal soldAudit) {
        this.soldAudit = soldAudit;
    }

    public Instant getCreatedAtAudit() {
        return createdAtAudit;
    }

    public void setCreatedAtAudit(Instant createdAtAudit) {
        this.createdAtAudit = createdAtAudit;
    }

    public Instant getUpdatedAtAudit() {
        return updatedAtAudit;
    }

    public void setUpdatedAtAudit(Instant updatedAtAudit) {
        this.updatedAtAudit = updatedAtAudit;
    }

    public static SaleAudit fromSale(Sale sale) {
        SaleAudit saleAudit = new SaleAudit();
        saleAudit.idAudit = sale.getId();
        saleAudit.soldAudit = sale.getSold();
        saleAudit.statusAudit = sale.getStatus();
        saleAudit.createdAtAudit = sale.getCreatedAt();
        saleAudit.updatedAtAudit = sale.getUpdatedAt();

        return saleAudit;
    }
}

