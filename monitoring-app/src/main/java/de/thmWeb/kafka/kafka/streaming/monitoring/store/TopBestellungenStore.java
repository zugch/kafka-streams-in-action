package de.thmWeb.kafka.kafka.streaming.monitoring.store;

import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TopBestellungenStore {

    private final List<BestellungEvent> bestellungEventList = new ArrayList<>();

    public void addBestellung(BestellungEvent bestellungEvent) {
        bestellungEventList.add(bestellungEvent);
    }

    public List<BestellungEvent> getTopbestellungen() {
        final List<BestellungEvent> bestellungEvents = new ArrayList<>(bestellungEventList);
        bestellungEvents.sort(new BestellungEventComparator());

        return bestellungEvents;

    }
}
