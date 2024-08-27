package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import org.apache.kafka.streams.StreamsBuilder;

public interface ExampleTopology {

    void createTopology(final StreamsBuilder builder);
}
