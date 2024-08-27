package de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde;

import de.thmWeb.kafka.kafka.streaming.events.*;
import org.apache.kafka.common.serialization.Serde;

public class SerdeFactory {

    public static Serde<KundeEvent> serdeForKundeEvent() {
        final CustomJsonSerde<KundeEvent> orderEventCustomJsonSerde = new CustomJsonSerde<>(KundeEvent.class);
        return orderEventCustomJsonSerde.get();
    }

    public static Serde<ArtikelEvent> serdeForArtikelEvent() {
        final CustomJsonSerde<ArtikelEvent> orderEventCustomJsonSerde = new CustomJsonSerde<>(ArtikelEvent.class);
        return orderEventCustomJsonSerde.get();
    }

    public static Serde<BestellungEvent> serdeForBestellungEvent() {
        final CustomJsonSerde<BestellungEvent> orderEventCustomJsonSerde = new CustomJsonSerde<>(BestellungEvent.class);
        return orderEventCustomJsonSerde.get();
    }

    public static Serde<BestellPositionEvent> serdeForBestellPositionEvent() {
        final CustomJsonSerde<BestellPositionEvent> orderEventCustomJsonSerde = new CustomJsonSerde<>(BestellPositionEvent.class);
        return orderEventCustomJsonSerde.get();
    }

    public static Serde<AggregatedKundeEvent> serdeForAggregatedKundeEvent() {
        final CustomJsonSerde<AggregatedKundeEvent> orderEventCustomJsonSerde = new CustomJsonSerde<>(AggregatedKundeEvent.class);
        return orderEventCustomJsonSerde.get();
    }

    public static Serde<LagerEvent> serdeForLagerEvent() {
        final CustomJsonSerde<LagerEvent> orderEventCustomJsonSerde = new CustomJsonSerde<>(LagerEvent.class);
        return orderEventCustomJsonSerde.get();
    }


}
