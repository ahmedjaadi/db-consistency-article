package com.consistency.example.consistencydb.repeatableread;

import com.consistency.example.consistencydb.domain.SaleStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RepeatableReadConsumer {
    @Autowired
    RepeatableReadService repeatableReadService;

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consume(ConsumerRecord<String, String> record) {
        System.out.println("Updating contract " + record.value() );
        repeatableReadService.updateSaleStatus(Long.parseLong(record.value()), SaleStatus.PROCESSING);
    }
}
