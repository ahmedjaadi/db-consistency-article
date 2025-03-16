package com.consistency.example.consistencydb.domain.dto;

import com.consistency.example.consistencydb.domain.SaleStatus;

public class SaleStatusCount {
    private SaleStatus status;
    private Long count;

    public SaleStatusCount(SaleStatus status, Long count) {
        this.status = status;
        this.count = count;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public Long getCount() {
        return count;
    }
}
