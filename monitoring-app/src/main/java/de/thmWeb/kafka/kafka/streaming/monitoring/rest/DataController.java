package de.thmWeb.kafka.kafka.streaming.monitoring.rest;

import de.thmWeb.kafka.kafka.streaming.events.AggregatedKundeEvent;
import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import de.thmWeb.kafka.kafka.streaming.monitoring.store.AggregatedKundenEventStore;
import de.thmWeb.kafka.kafka.streaming.monitoring.store.TopBestellungenStore;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
public class DataController {

    private final TopBestellungenStore topBestellungenStore;
    private final AggregatedKundenEventStore aggregatedKundenEventStore;

    public DataController(TopBestellungenStore topBestellungenStore, AggregatedKundenEventStore aggregatedKundenEventStore) {
        this.topBestellungenStore = topBestellungenStore;
        this.aggregatedKundenEventStore = aggregatedKundenEventStore;
    }

    @GetMapping("/api/test")
    public List<BestellungEvent> test() {
        BestellungEvent b1 = BestellungEvent.builder().kundenIdx("111").gesamtSumme(200).build();
        BestellungEvent b2 = BestellungEvent.builder().kundenIdx("222").gesamtSumme(100).build();
        return Arrays.asList(b1, b2);
    }

    @GetMapping("/api/topbestellungen")
    public List<BestellungEvent> getTopBestellungen() {
        return topBestellungenStore.getTopbestellungen();
    }

    @GetMapping("/api/kundenbestellungen")
    public List<AggregatedKundeEvent> getAggregatedKunden() {
        return aggregatedKundenEventStore.getAggregatedKundeEvents();
    }
}
