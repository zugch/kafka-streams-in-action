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


    }
}