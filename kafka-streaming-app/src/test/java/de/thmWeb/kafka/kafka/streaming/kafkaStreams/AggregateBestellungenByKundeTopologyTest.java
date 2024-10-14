package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import de.thmWeb.kafka.kafka.streaming.events.AggregatedKundeEvent;
import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import de.thmWeb.kafka.kafka.streaming.events.KundeEvent;
import de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde.SerdeFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class AggregateBestellungenByKundeTopologyTest {

    private TopologyTestDriver testDriver;
    private TestInputTopic<String, BestellungEvent> inputTopicBestellungen;

    private TestInputTopic<String, KundeEvent> inputTopicKunden;
    private TestOutputTopic<String, AggregatedKundeEvent> outputTopic;

    @BeforeEach
    void setUp() {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "test01");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092");

        final StreamsBuilder builder = new StreamsBuilder();

        final AggregateBestellungenByKundeTopology aggregateBestellungenByKundeTopology = new AggregateBestellungenByKundeTopology();
        aggregateBestellungenByKundeTopology.createTopology(builder);

        final Topology topology = builder.build();
        testDriver = new TopologyTestDriver(topology, streamsConfiguration);
        inputTopicKunden = testDriver
                .createInputTopic("kunden", Serdes.String().serializer(), SerdeFactory.serdeForKundeEvent().serializer());
        inputTopicBestellungen = testDriver
                .createInputTopic("bestellungen", Serdes.String().serializer(), SerdeFactory.serdeForBestellungEvent().serializer());
        outputTopic = testDriver
                .createOutputTopic("bestellungen-aggregiert-pro-kunde", Serdes.String().deserializer(), SerdeFactory.serdeForAggregatedKundeEvent().deserializer());
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void createTopology() {


    }
}
