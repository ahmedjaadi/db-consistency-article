package com.consistency.example.consistencydb.messaging;

import com.consistency.example.consistencydb.domain.dto.EventType;
import com.consistency.example.consistencydb.domain.dto.SaleEvent;
import com.consistency.example.consistencydb.domain.entity.SaleStatus;
import com.consistency.example.consistencydb.domain.service.SaleService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SaleEventConsumer {
    @Autowired
    SaleService saleService;

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consume(ConsumerRecord<String, String> record) {
        System.out.println("Updating sale " + record.value() );

        var saleEvent = SaleEvent.toSaleEvent(record.value());

        assert saleEvent != null;

        Boolean idempotency = saleEvent.type().equals(EventType.PAGINATION_ZERO);

        saleService.processSaleEvent(saleEvent.id(), idempotency);
    }
}
