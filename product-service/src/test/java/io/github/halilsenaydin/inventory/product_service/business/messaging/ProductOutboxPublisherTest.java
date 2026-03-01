package io.github.halilsenaydin.inventory.product_service.business.messaging;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts.ProductOutboxRepository;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductOutbox;

@ExtendWith(MockitoExtension.class)
public class ProductOutboxPublisherTest {
    @Mock
    private ProductOutboxRepository outboxRepository;

    @Mock
    private ProductOutboxEventPublisher outboxEventPublisher;

    @InjectMocks
    private ProductOutboxPublisher outboxPublisher;

    private ProductOutbox outbox1;
    private ProductOutbox outbox2;
    private List<ProductOutbox> outboxList;
    private Page<ProductOutbox> page;

    @BeforeEach
    void setUp() {
        outbox1 = new ProductOutbox();
        outbox2 = new ProductOutbox();
        outboxList = List.of(outbox1, outbox2);

        page = new PageImpl<>(outboxList);
    }

    @Test
    void publishEvents_shouldNotCallPublisher_whenNoUnprocessedEvents() {
        when(outboxRepository.findByProcessedFalse(any()))
                .thenReturn(Page.empty());

        outboxPublisher.publishEvents();

        verify(outboxEventPublisher, never()).publishEvent(any());
    }

    @Test
    void publishEvents_shouldPublishAllEvents_whenUnprocessedEventsExist() {
        when(outboxRepository.findByProcessedFalse(any()))
                .thenReturn(page)
                .thenReturn(Page.empty());

        outboxPublisher.publishEvents();

        verify(outboxEventPublisher, times(2)).publishEvent(any());
    }

    @Test
    void publishEvents_shouldProcessMultipleBatches_whenMoreThanBatchSizeExists() {
        List<ProductOutbox> batch1 = List.of(new ProductOutbox(), new ProductOutbox());
        List<ProductOutbox> batch2 = List.of(new ProductOutbox());

        Page<ProductOutbox> firstPage = new PageImpl<>(batch1, PageRequest.of(0, 50), 3);
        Page<ProductOutbox> secondPage = new PageImpl<>(batch2, PageRequest.of(0, 50), 3);

        when(outboxRepository.findByProcessedFalse(any(PageRequest.class)))
                .thenReturn(firstPage)
                .thenReturn(secondPage)
                .thenReturn(Page.empty());

        outboxPublisher.publishEvents();

        verify(outboxEventPublisher, times(3)).publishEvent(any());
    }
}
