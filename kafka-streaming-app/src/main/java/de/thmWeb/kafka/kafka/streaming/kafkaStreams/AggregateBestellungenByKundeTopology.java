package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import de.thmWeb.kafka.kafka.streaming.events.AggregatedKundeEvent;
import de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde.SerdeFactory;
import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import de.thmWeb.kafka.kafka.streaming.events.KundeEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.stereotype.Component;

@Component
public class AggregateBestellungenByKundeTopology implements ExampleTopology {

    @Override
    public void createTopology(final StreamsBuilder builder) {
        // Kunden in KTable einlesen aus Topic "kunden"

        // Bestellungen in Stream einlesen aus Topic "bestellungen"

        // Bestellungen mit Key=Kunde versehen

        // Bestellungen gruppieren anhand KundenIdx

        // Bestellungen aggregieren

        // ValueJoiner erstellen

        // Join AggregierteBestellungen mit Kunden

        // Ausgabe auf Topic "bestellungen-aggregiert-pro-kunde"

    }
}