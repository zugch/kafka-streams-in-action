package de.thmWeb.kafka.kafka.streaming.monitoring.store;

import de.thmWeb.kafka.kafka.streaming.events.AggregatedKundeEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AggregatedKundenEventStore {

    private final Map<String, AggregatedKundeEvent> aggregatedKundeEventMap = new HashMap<>();

    public void addAggregatedKundeEvent(AggregatedKundeEvent aggregatedKundeEvent) {
        aggregatedKundeEventMap.put(aggregatedKundeEvent.getKundenIdx(), aggregatedKundeEvent);
    }

    public List<AggregatedKundeEvent> getAggregatedKundeEvents() {
        List<AggregatedKundeEvent> aggregatedKundeEventList = new ArrayList<>(aggregatedKundeEventMap.values());
        aggregatedKundeEventList.sort(new AggregatedKundeEventComparator());

        return aggregatedKundeEventList;
    }

}
