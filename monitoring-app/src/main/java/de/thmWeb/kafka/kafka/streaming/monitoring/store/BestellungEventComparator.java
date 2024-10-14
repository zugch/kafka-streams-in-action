package de.thmWeb.kafka.kafka.streaming.monitoring.store;

import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;

import java.util.Comparator;

public class BestellungEventComparator implements Comparator<BestellungEvent> {
    @Override
    public int compare(BestellungEvent be1, BestellungEvent be2) {
        return be1.getGesamtSumme() > be2.getGesamtSumme() ? -1 : 1;
    }
}
