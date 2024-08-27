package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Component
public class StreamingStarter implements CommandLineRunner {

    private final List<ExampleTopology> topologies;

    private final String bootstrapServers;

    public StreamingStarter(List<ExampleTopology> topologies, @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        this.topologies = topologies;
        this.bootstrapServers = bootstrapServers;
    }

    @Override
    public void run(String... args) throws Exception {

        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkaStreaming" + UUID.randomUUID().toString());
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);
        final StreamsBuilder builder = new StreamsBuilder();
        topologies.forEach(t -> t.createTopology(builder));


        // start streaming
        final Topology streamingTopology = builder.build();
        final KafkaStreams kafkaStreams = new KafkaStreams(streamingTopology, streamsConfiguration);
        kafkaStreams.start();


    }
}
