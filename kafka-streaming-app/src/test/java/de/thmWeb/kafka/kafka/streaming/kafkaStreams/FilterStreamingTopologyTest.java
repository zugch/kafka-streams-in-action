package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde.SerdeFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class FilterStreamingTopologyTest {

    private TopologyTestDriver testDriver;
    private TestInputTopic<String, BestellungEvent> inputTopic;
    private TestOutputTopic<String, BestellungEvent> outputTopic;

    @BeforeEach
    void setUp() {

        final StreamsBuilder builder = new StreamsBuilder();

        final FilterStreamingTopology filterStreamingTopology = new FilterStreamingTopology();
        filterStreamingTopology.createTopology(builder);


        final Topology topology = builder.build();
        testDriver = new TopologyTestDriver(topology, new Properties());
        inputTopic = testDriver
                .createInputTopic("bestellungen", Serdes.String().serializer(), SerdeFactory.serdeForBestellungEvent().serializer());
        outputTopic = testDriver
                .createOutputTopic("topbestellungen", Serdes.String().deserializer(), SerdeFactory.serdeForBestellungEvent().deserializer());

    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void testFilterTopology() {

        final BestellungEvent b01 = BestellungEvent.builder().idx("b01").gesamtSumme(249).build();

        final BestellungEvent b02 = BestellungEvent.builder().idx("b02").gesamtSumme(350).build();

        final BestellungEvent b03 = BestellungEvent.builder().idx("b03").gesamtSumme(420).build();

        final BestellungEvent b04 = BestellungEvent.builder().idx("b04").gesamtSumme(270).build();


        inputTopic.pipeInput(b01.getIdx(), b01);
        inputTopic.pipeInput(b02.getIdx(), b02);
        inputTopic.pipeInput(b03.getIdx(), b03);
        inputTopic.pipeInput(b04.getIdx(), b04);


        final Map<String, BestellungEvent> result = outputTopic.readKeyValuesToMap();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(350, result.get(b02.getIdx()).getGesamtSumme());
        Assertions.assertEquals(420, result.get(b03.getIdx()).getGesamtSumme());


    }
}