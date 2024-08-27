package de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.util.HashMap;
import java.util.Map;

public class CustomJsonSerde<T> {

    private Class<T> destinationClass;

    public CustomJsonSerde(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }


    public Serde<T> get() {
        final Map<String, Object> serdeProps = new HashMap<>();

        final Serializer<T> serializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", destinationClass);
        serializer.configure(serdeProps, false);

        final Deserializer<T> deserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", destinationClass);
        deserializer.configure(serdeProps, false);

        final Serde<T> serde = Serdes.serdeFrom(serializer, deserializer);

        return serde;

    }
}
