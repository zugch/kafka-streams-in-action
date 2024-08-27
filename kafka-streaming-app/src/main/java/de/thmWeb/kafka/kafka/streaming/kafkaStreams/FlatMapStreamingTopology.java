package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde.SerdeFactory;
import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import de.thmWeb.kafka.kafka.streaming.events.LagerEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlatMapStreamingTopology implements ExampleTopology {

    @Override
    public void createTopology(final StreamsBuilder builder) {

        // Bestellungen in Stream einlesen
        final KStream<String, BestellungEvent> bestellungenStream =
                builder.stream("bestellungen", Consumed.with(Serdes.String(), SerdeFactory.serdeForBestellungEvent()));

        // aus Bestellpositionen die LagerEvents erstellen
        final KStream<String, LagerEvent> lagerEventStream = bestellungenStream.flatMap((key, val) -> {
            final List<KeyValue<String, LagerEvent>> lagerEvents = new ArrayList<>();
            val.getBestellPositionen().forEach(bp -> lagerEvents.add(
                    KeyValue.pair(bp.getIdx(),
                            LagerEvent.builder().anzahl(bp.getAnzahlArtikel()).artikelIdx(bp.getArtikelEvent().getIdx()).build()))
            );

            return lagerEvents;
        });

        // Ausgabe auf Topic
        lagerEventStream.to("lagerverwaltung", Produced.with(Serdes.String(), SerdeFactory.serdeForLagerEvent()));


    }


}
