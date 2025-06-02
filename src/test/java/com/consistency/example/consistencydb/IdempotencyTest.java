package com.consistency.example.consistencydb;

import com.consistency.example.consistencydb.domain.entity.Sale;
import com.consistency.example.consistencydb.domain.entity.SaleStatus;
import com.consistency.example.consistencydb.domain.service.SaleAuditService;
import com.consistency.example.consistencydb.domain.service.SaleService;
import com.consistency.example.consistencydb.repository.SaleRepository;
import com.consistency.example.consistencydb.testcontainers.PostgresTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class IdempotencyTest {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleAuditService saleAuditService;

    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setUp() {
        saleRepository.deleteAll();
        Sale sale = new Sale();
        sale.setStatus(SaleStatus.NOT_INITIALIZED);
        sale.setSold(BigDecimal.valueOf(0));
        saleRepository.save(sale);
    }

    @Test
    void simulateConcurrentUpdatesWithOCC() throws Exception {
        long saleId = 1L;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            saleService.processSaleEvent(saleId, true);
            return "Job1 succeeded";
        });

         executor.submit(() -> {
            Thread.sleep(10);
            saleService.processSaleEvent(saleId, true);
            return "Job2 succeeded";
        });

        executor.shutdown();

        assertThat(saleAuditService.findAll()).hasSize(1);
    }
}
