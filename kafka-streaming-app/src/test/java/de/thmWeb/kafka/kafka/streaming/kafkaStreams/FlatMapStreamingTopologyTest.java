package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import de.thmWeb.kafka.kafka.streaming.events.ArtikelEvent;
import de.thmWeb.kafka.kafka.streaming.events.BestellPositionEvent;
import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import de.thmWeb.kafka.kafka.streaming.events.LagerEvent;
import de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde.SerdeFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

class FlatMapStreamingTopologyTest {

    private TopologyTestDriver testDriver;
    private TestInputTopic<String, BestellungEvent> inputTopic;
    private TestOutputTopic<String, LagerEvent> outputTopic;

    @BeforeEach
    void setUp() {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "test01");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092");

        final StreamsBuilder builder = new StreamsBuilder();

        final FlatMapStreamingTopology flatMapStreamingTopology = new FlatMapStreamingTopology();
        flatMapStreamingTopology.createTopology(builder);

        final Topology topology = builder.build();
        testDriver = new TopologyTestDriver(topology, streamsConfiguration);
        inputTopic = testDriver
                .createInputTopic("bestellungen", Serdes.String().serializer(), SerdeFactory.serdeForBestellungEvent().serializer());
        outputTopic = testDriver
                .createOutputTopic("lagerverwaltung", Serdes.String().deserializer(), SerdeFactory.serdeForLagerEvent().deserializer());
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void createTopology() {
        final BestellungEvent b01 = BestellungEvent.builder().idx("b01").gesamtSumme(20).bestellPositionen(
                Arrays.asList(
                        BestellPositionEvent.builder().idx("bp011").anzahlArtikel(2).artikelEvent(ArtikelEvent.builder().idx("art01").build()).build(),
                        BestellPositionEvent.builder().idx("bp012").anzahlArtikel(1).artikelEvent(ArtikelEvent.builder().idx("art02").build()).build()
                )
        ).build();

        final BestellungEvent b02 = BestellungEvent.builder().idx("b02").gesamtSumme(10).bestellPositionen(
                Arrays.asList(
                        BestellPositionEvent.builder().idx("bp021").anzahlArtikel(2).artikelEvent(ArtikelEvent.builder().idx("art03").build()).build()
                )
        ).build();

        inputTopic.pipeInput(b01.getIdx(), b01);
        inputTopic.pipeInput(b02.getIdx(), b02);

        final Map<String, LagerEvent> result = outputTopic.readKeyValuesToMap();
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("art01", result.get("bp011").getArtikelIdx());
        Assertions.assertEquals(2, result.get("bp011").getAnzahl());
        Assertions.assertEquals("art02", result.get("bp012").getArtikelIdx());
        Assertions.assertEquals(1, result.get("bp012").getAnzahl());
        Assertions.assertEquals("art03", result.get("bp021").getArtikelIdx());
        Assertions.assertEquals(2, result.get("bp021").getAnzahl());
    }
}
