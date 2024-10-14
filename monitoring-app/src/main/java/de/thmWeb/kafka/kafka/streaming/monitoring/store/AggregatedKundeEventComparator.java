package de.thmWeb.kafka.kafka.streaming.monitoring.store;

import de.thmWeb.kafka.kafka.streaming.events.AggregatedKundeEvent;

import java.util.Comparator;

public class AggregatedKundeEventComparator implements Comparator<AggregatedKundeEvent> {
    @Override
    public int compare(AggregatedKundeEvent ake1, AggregatedKundeEvent ake2) {
        return ake1.getGesamtSumme() > ake2.getGesamtSumme() ? -1 : 1;
    }
}
