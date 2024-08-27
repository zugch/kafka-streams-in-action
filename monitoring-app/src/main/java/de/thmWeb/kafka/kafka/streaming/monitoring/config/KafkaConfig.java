package de.thmWeb.kafka.kafka.streaming.monitoring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thmWeb.kafka.kafka.streaming.events.AggregatedKundeEvent;
import de.thmWeb.kafka.kafka.streaming.events.KundeEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
@Slf4j
public class KafkaConfig {

    private final ObjectMapper objectMapper;
    private final KafkaProperties kafkaProperties;

    public KafkaConfig(ObjectMapper objectMapper, KafkaProperties kafkaProperties) {
        this.objectMapper = objectMapper;
        this.kafkaProperties = kafkaProperties;
    }



    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KundeEvent> kundeEventKafkaListenerContainerBatchFactory() {
        return createKafkaListenerContainerFactory(KundeEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AggregatedKundeEvent> aggregatedKundeEventKafkaListenerContainerBatchFactory() {
        return createKafkaListenerContainerFactory(AggregatedKundeEvent.class);
    }


    private <T> ConcurrentKafkaListenerContainerFactory<String, T> createKafkaListenerContainerFactory(final Class<T> type) {
        final ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createKafkaConsumerFactory(type));

        return factory;
    }

    private <T> ConsumerFactory<String, T> createKafkaConsumerFactory(final Class<T> type) {
        final Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties(null);

        final StringDeserializer stringDeserializer = new StringDeserializer();
        stringDeserializer.configure(consumerProperties, true);

        final JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<>(type, objectMapper);
        jsonDeserializer.configure(consumerProperties, false);

        return new DefaultKafkaConsumerFactory<>(consumerProperties,
                new ErrorHandlingDeserializer<>(stringDeserializer),
                new ErrorHandlingDeserializer<>(jsonDeserializer));
    }
}
