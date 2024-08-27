package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import de.thmWeb.kafka.kafka.streaming.events.ArtikelEvent;
import de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde.SerdeFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

class BranchTopologyTest {

    private TopologyTestDriver testDriver;
    private TestInputTopic<String, ArtikelEvent> inputTopic;
    private TestOutputTopic<String, ArtikelEvent> outputTopicLaptops;
    private TestOutputTopic<String, ArtikelEvent> outputTopicTablets;

    @BeforeEach
    void setUp() {

        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "test01");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092");


        final StreamsBuilder builder = new StreamsBuilder();

        final BranchTopology branchTopology = new BranchTopology();
        branchTopology.createTopology(builder);


        final Topology topology = builder.build();
        testDriver = new TopologyTestDriver(topology, streamsConfiguration);
        inputTopic = testDriver
                .createInputTopic("artikel", Serdes.String().serializer(), SerdeFactory.serdeForArtikelEvent().serializer());


        outputTopicLaptops = testDriver
                .createOutputTopic("artikel-laptops", Serdes.String().deserializer(), SerdeFactory.serdeForArtikelEvent().deserializer());
        outputTopicTablets = testDriver
                .createOutputTopic("artikel-tablets", Serdes.String().deserializer(), SerdeFactory.serdeForArtikelEvent().deserializer());

    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void createTopology() {

        final ArtikelEvent art01 = ArtikelEvent.builder().idx("art01").kategorie("Tablets").build();
        final ArtikelEvent art02 = ArtikelEvent.builder().idx("art02").kategorie("Laptops").build();
        final ArtikelEvent art03 = ArtikelEvent.builder().idx("art03").kategorie("Tablets").build();
        final ArtikelEvent art04 = ArtikelEvent.builder().idx("art04").kategorie("Laptops").build();
        final ArtikelEvent art05 = ArtikelEvent.builder().idx("art05").kategorie("Tablets").build();


        inputTopic.pipeInput(art01.getIdx(), art01);
        inputTopic.pipeInput(art02.getIdx(), art02);
        inputTopic.pipeInput(art03.getIdx(), art03);
        inputTopic.pipeInput(art04.getIdx(), art04);
        inputTopic.pipeInput(art05.getIdx(), art05);


        final Map<String, ArtikelEvent> resultComputer = outputTopicLaptops.readKeyValuesToMap();
        Assertions.assertEquals(2, resultComputer.size());

        final Map<String, ArtikelEvent> resultSmartphones = outputTopicTablets.readKeyValuesToMap();
        Assertions.assertEquals(3, resultSmartphones.size());


    }

}
