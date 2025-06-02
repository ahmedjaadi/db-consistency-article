package com.consistency.example.consistencydb.domain.service.paginationzero;

import com.consistency.example.consistencydb.domain.dto.EventType;
import com.consistency.example.consistencydb.domain.dto.SaleEvent;
import com.consistency.example.consistencydb.domain.entity.Sale;
import com.consistency.example.consistencydb.domain.entity.SaleStatus;
import com.consistency.example.consistencydb.domain.service.SaleService;
import com.consistency.example.consistencydb.messaging.SaleEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class PaginationZeroService {
    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleEventProducer saleEventProducer;

    public void process(Boolean fixed) {
        Pageable pagination = PageRequest.of(0, 100);
        Page<Sale> pageSale;

        saleService.updateAllCreatedAt();

        do {
            pageSale = saleService.findAllByStatus(SaleStatus.NOT_INITIALIZED, pagination);

            pageSale.forEach(
                sale -> {
                    var event = new SaleEvent(sale.getId(), EventType.PAGINATION_ZERO);
                    saleEventProducer.sendMessage("my-topic", "key", event.toString());
                }
            );

            if (!fixed) {
                pagination = pageSale.getPageable().next();
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    System.out.println("Erro no thread sleep");
                }
            }
        } while (pageSale.hasNext());
    }
}
