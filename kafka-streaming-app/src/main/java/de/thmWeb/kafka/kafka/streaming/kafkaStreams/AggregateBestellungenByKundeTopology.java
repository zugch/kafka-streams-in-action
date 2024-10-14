package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.stereotype.Component;

@Component
public class AggregateBestellungenByKundeTopology implements ExampleTopology {

    @Override
    public void createTopology(final StreamsBuilder builder) {
        // Bestellungen in Stream einlesen aus Topic "bestellungen"


        // Kunden in KTable einlesen aus Topic "kunden"


        // Bestellungen mit Key=Kunde versehen


        // Bestellungen gruppieren anhand KundenIdx


        // Bestellungen aggregieren


        // ValueJoiner erstellen


        // Join AggregierteBestellungen mit Kunden


        // Ausgabe auf Topic "bestellungen-aggregiert-pro-kunde"


    }
}
