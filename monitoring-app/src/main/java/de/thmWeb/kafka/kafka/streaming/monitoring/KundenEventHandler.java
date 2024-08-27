package de.thmWeb.kafka.kafka.streaming.monitoring;

import de.thmWeb.kafka.kafka.streaming.events.AggregatedKundeEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KundenEventHandler {

    private final MeterRegistry meterRegistry;

    public KundenEventHandler(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }


    @KafkaListener(topics = "bestellungen-aggregiert-pro-kunde", containerFactory = "aggregatedKundeEventKafkaListenerContainerBatchFactory")
    public void listenToAggregatedKundeEvent(@Payload final AggregatedKundeEvent aggregatedKundeEvent) {
        log.debug("aggregatedKundeEvent: {}", aggregatedKundeEvent);
        String kundenIdx = aggregatedKundeEvent.getKundenIdx();

        Counter counter = meterRegistry.counter("aggregatedKundeEventCounter_total", "kundenIdx", aggregatedKundeEvent.getKunde().getIdx(), "email", aggregatedKundeEvent.getKunde().getEmail());
        counter.increment(Double.valueOf(aggregatedKundeEvent.getGesamtSumme()));

    }


}
