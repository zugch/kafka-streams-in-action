package de.thmWeb.kafka.kafka.streaming.monitoring;

import de.thmWeb.kafka.kafka.streaming.events.AggregatedKundeEvent;
import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import de.thmWeb.kafka.kafka.streaming.monitoring.store.AggregatedKundenEventStore;
import de.thmWeb.kafka.kafka.streaming.monitoring.store.TopBestellungenStore;
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
    private final TopBestellungenStore topBestellungenStore;
    private final AggregatedKundenEventStore aggregatedKundenEventStore;

    public KundenEventHandler(MeterRegistry meterRegistry, TopBestellungenStore topBestellungenStore, AggregatedKundenEventStore aggregatedKundenEventStore) {
        this.meterRegistry = meterRegistry;
        this.topBestellungenStore = topBestellungenStore;
        this.aggregatedKundenEventStore = aggregatedKundenEventStore;
    }


    @KafkaListener(topics = "bestellungen-aggregiert-pro-kunde", containerFactory = "aggregatedKundeEventKafkaListenerContainerBatchFactory")
    public void listenToAggregatedKundeEvent(@Payload final AggregatedKundeEvent aggregatedKundeEvent) {
        log.debug("aggregatedKundeEvent: {}", aggregatedKundeEvent);
        aggregatedKundenEventStore.addAggregatedKundeEvent(aggregatedKundeEvent);

        Counter counter = meterRegistry.counter("aggregatedKundeEventCounter_total", "kundenIdx", aggregatedKundeEvent.getKunde().getIdx(), "email", aggregatedKundeEvent.getKunde().getEmail());
        counter.increment(Double.valueOf(aggregatedKundeEvent.getGesamtSumme()));

    }

    @KafkaListener(topics = "topbestellungen", containerFactory = "bestellungEventKafkaListenerContainerBatchFactory")
    public void listenToTopBestellungEvent(@Payload final BestellungEvent bestellungEvent) {
        log.debug("bestellungEvent: {}", bestellungEvent);
        topBestellungenStore.addBestellung(bestellungEvent);

        Counter counter = meterRegistry.counter("bestellungEventCounter_total", "kundenIdx", bestellungEvent.getKundenIdx());
        counter.increment(Double.valueOf(bestellungEvent.getGesamtSumme()));

    }


}
