package de.thmWeb.kafka.kafka.streaming.bestellungenSimulator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class KafkaHandler {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaHandler(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendEventToKafka(String topic, String eventIdx, Object model, Map<String, String> headers) {


        MessageBuilder<Object> objectMessageBuilder = MessageBuilder
                .withPayload(model)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, eventIdx);

        headers.entrySet().forEach(entry -> {
            objectMessageBuilder.setHeader(entry.getKey(), entry.getValue());
        });

        final Message<Object> message = objectMessageBuilder
                .build();

        CompletableFuture<SendResult<String, Object>> send = kafkaTemplate.send(message);
        try {
            SendResult<String, Object> stringObjectSendResult = send.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }


    }
}
