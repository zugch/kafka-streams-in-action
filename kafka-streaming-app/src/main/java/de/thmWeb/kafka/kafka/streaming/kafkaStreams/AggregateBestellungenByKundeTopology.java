package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import de.thmWeb.kafka.kafka.streaming.events.AggregatedKundeEvent;
import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import de.thmWeb.kafka.kafka.streaming.events.KundeEvent;
import de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde.SerdeFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.stereotype.Component;

@Component
public class AggregateBestellungenByKundeTopology implements ExampleTopology {

    @Override
    public void createTopology(final StreamsBuilder builder) {
        // Bestellungen in Stream einlesen aus Topic "bestellungen"
        KStream<String, BestellungEvent> bestellungenStream = builder.stream("bestellungen",
                Consumed.with(Serdes.String(), SerdeFactory.serdeForBestellungEvent()));


        // Kunden in KTable einlesen aus Topic "kunden"
        KTable<String, KundeEvent> kundenTable = builder.table("kunden", Consumed.with(Serdes.String(), SerdeFactory.serdeForKundeEvent()));

        // Bestellungen mit Key=Kunde versehen
        KStream<String, BestellungEvent> bestellungenWithNewKey = bestellungenStream.selectKey((k, v) -> v.getKundenIdx());

        // Bestellungen gruppieren anhand KundenIdx
        KGroupedStream<String, BestellungEvent> groupedStream =
                bestellungenWithNewKey.groupByKey(Grouped.with(Serdes.String(), SerdeFactory.serdeForBestellungEvent()));


        // Bestellungen aggregieren
        KTable<String, AggregatedKundeEvent> aggregatedKundeEventTable = groupedStream.aggregate(() -> new AggregatedKundeEvent(),
                (key, value, aggregatedKundeEvent) -> {
                    aggregatedKundeEvent.setGesamtSumme(aggregatedKundeEvent.getGesamtSumme() + value.getGesamtSumme());
                    return aggregatedKundeEvent;
                },
                Materialized.with(Serdes.String(), SerdeFactory.serdeForAggregatedKundeEvent())
        );


        // ValueJoiner erstellen
        ValueJoiner<AggregatedKundeEvent, KundeEvent, AggregatedKundeEvent> valueJoiner = new ValueJoiner<AggregatedKundeEvent, KundeEvent, AggregatedKundeEvent>() {
            @Override
            public AggregatedKundeEvent apply(AggregatedKundeEvent aggregatedKundeEvent, KundeEvent kundeEvent) {
                aggregatedKundeEvent.setKundenIdx(kundeEvent.getIdx());
                aggregatedKundeEvent.setKunde(kundeEvent);
                return aggregatedKundeEvent;
            }
        };


        // Join AggregierteBestellungen mit Kunden
        KStream<String, AggregatedKundeEvent> joinedBestellungen = aggregatedKundeEventTable.toStream().join(kundenTable, valueJoiner,
                Joined.with(Serdes.String(), SerdeFactory.serdeForAggregatedKundeEvent(), SerdeFactory.serdeForKundeEvent()));

        // Ausgabe auf Topic "bestellungen-aggregiert-pro-kunde"
        joinedBestellungen.to("bestellungen-aggregiert-pro-kunde", Produced.with(Serdes.String(), SerdeFactory.serdeForAggregatedKundeEvent()));

    }
}
