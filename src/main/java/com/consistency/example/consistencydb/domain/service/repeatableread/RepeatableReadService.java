package com.consistency.example.consistencydb.domain.service.repeatableread;

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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class RepeatableReadService {
    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleEventProducer saleEventProducer;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void process() {
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

            try {
                Thread.sleep(40);
            } catch (InterruptedException ex) {
                System.out.println("Erro no thread sleep");
            }

            pagination = pageSale.getPageable().next();
        } while (pageSale.hasNext());
    }
}
