package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde.SerdeFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.stereotype.Component;

@Component
public class FilterStreamingTopology implements ExampleTopology {

    @Override
    public void createTopology(final StreamsBuilder builder) {

        // Bestellungen in Stream einlesen

        // Filtern aller Bestellungen mit Gesammtsummer > 300

        // Ausgabe auf Topic


    }
}
