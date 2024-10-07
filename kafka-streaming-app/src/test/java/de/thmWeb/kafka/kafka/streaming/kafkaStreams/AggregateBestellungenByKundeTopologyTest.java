package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import de.thmWeb.kafka.kafka.streaming.events.AggregatedKundeEvent;
import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import de.thmWeb.kafka.kafka.streaming.events.KundeEvent;
import de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde.SerdeFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
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
        final KundeEvent k01 = KundeEvent.builder().idx("k01").build();
        final KundeEvent k02 = KundeEvent.builder().idx("k02").build();

        inputTopicKunden.pipeInput(k01.getIdx(), k01);
        inputTopicKunden.pipeInput(k02.getIdx(), k02);

        final BestellungEvent b01 = BestellungEvent.builder().idx("b01").kundenIdx("k01").gesamtSumme(100).build();
        final BestellungEvent b02 = BestellungEvent.builder().idx("b02").kundenIdx("k02").gesamtSumme(200).build();
        final BestellungEvent b03 = BestellungEvent.builder().idx("b03").kundenIdx("k01").gesamtSumme(300).build();
        final BestellungEvent b04 = BestellungEvent.builder().idx("b04").kundenIdx("k02").gesamtSumme(400).build();
        final BestellungEvent b05 = BestellungEvent.builder().idx("b05").kundenIdx("k01").gesamtSumme(500).build();

        inputTopicBestellungen.pipeInput(b01.getIdx(), b01);
        inputTopicBestellungen.pipeInput(b02.getIdx(), b02);
        inputTopicBestellungen.pipeInput(b03.getIdx(), b03);
        inputTopicBestellungen.pipeInput(b04.getIdx(), b04);
        inputTopicBestellungen.pipeInput(b05.getIdx(), b05);

        final Map<String, AggregatedKundeEvent> result = outputTopic.readKeyValuesToMap();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(900, result.get("k01").getGesamtSumme());
        Assertions.assertEquals(600, result.get("k02").getGesamtSumme());

        final BestellungEvent b06 = BestellungEvent.builder().idx("b06").kundenIdx("k01").gesamtSumme(50).build();
        inputTopicBestellungen.pipeInput(b06.getIdx(), b06);

        final Map<String, AggregatedKundeEvent> result2 = outputTopic.readKeyValuesToMap();
        Assertions.assertEquals(1, result2.size());
        Assertions.assertEquals(950, result2.get("k01").getGesamtSumme());


    }
}
