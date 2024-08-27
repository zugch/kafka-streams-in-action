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
        // Kunden in KTable einlesen
        final KTable<String, KundeEvent> kundenTable = builder.table("kunden",
                Consumed.with(Serdes.String(), SerdeFactory.serdeForKundeEvent()));

        // Bestellungen in Stream einlesen
        final KStream<String, BestellungEvent> bestellungenStream =
                builder.stream("bestellungen", Consumed.with(Serdes.String(), SerdeFactory.serdeForBestellungEvent()));

        // Bestellungen mit Key=Kunde versehen
        final KStream<String, BestellungEvent> bestellungenWithNewKey = bestellungenStream.selectKey((key, val) -> val.getKundenIdx());


        // Bestellungen gruppieren anhand KundenIdx
        final KGroupedStream<String, BestellungEvent> groupedOrderStream =
                bestellungenWithNewKey.groupByKey(Grouped.with(Serdes.String(), SerdeFactory.serdeForBestellungEvent()));

        // Bestellungen aggregieren
        final KTable<String, BestellungEvent> aggregatedBestellungenByKunde = groupedOrderStream.aggregate(() -> new BestellungEvent(),
                (key, value, bestellungEvent) -> {
                    bestellungEvent.setGesamtSumme(bestellungEvent.getGesamtSumme() + value.getGesamtSumme());
                    return bestellungEvent;
                },
                Materialized.with(Serdes.String(), SerdeFactory.serdeForBestellungEvent()));


        final ValueJoiner<BestellungEvent, KundeEvent, AggregatedKundeEvent> valueJoiner = new ValueJoiner<>() {
            @Override
            public AggregatedKundeEvent apply(BestellungEvent bestellungEvent, KundeEvent kundeEvent) {
                return AggregatedKundeEvent.builder()
                        .kunde(kundeEvent)
                        .kundenIdx(kundeEvent.getIdx())
                        .gesamtSumme(bestellungEvent.getGesamtSumme())
                        .build();
            }
        };
        KStream<String, AggregatedKundeEvent> joinedBestellungen = aggregatedBestellungenByKunde.toStream().join(kundenTable, valueJoiner,
                Joined.with(Serdes.String(), SerdeFactory.serdeForBestellungEvent(), SerdeFactory.serdeForKundeEvent()));

        // Ausgabe auf Topic
        joinedBestellungen.to("bestellungen-aggregiert-pro-kunde", Produced.with(Serdes.String(), SerdeFactory.serdeForAggregatedKundeEvent()));



    }
}
