package com.consistency.example.consistencydb.repeatableread;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RepeatableReadConsumer {
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consume(ConsumerRecord<String, String> record) {
        System.out.printf("Consumed message: Key=%s, Value=%s, Partition=%d, Offset=%d%n",
                record.key(), record.value(), record.partition(), record.offset());
    }
}
