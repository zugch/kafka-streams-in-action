package de.thmWeb.kafka.kafka.streaming.bestellungenSimulator.initData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import de.thmWeb.kafka.kafka.streaming.bestellungenSimulator.KafkaHandler;
import de.thmWeb.kafka.kafka.streaming.events.KundeEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@Component
public class KundenImporter {

    private final KafkaHandler kafkaHandler;

    public KundenImporter(KafkaHandler kafkaHandler) {
        this.kafkaHandler = kafkaHandler;
    }

    public void importKunden(final Integer numberOfKunden) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerForListOf(KundeEvent.class);

        URL resource = this.getClass().getClassLoader().getResource("user_katalog.json");
        List<KundeEvent> kundeEventList = objectReader.readValue(resource);
        for (int i = 0; i < numberOfKunden; i++) {
            KundeEvent kundeEvent = kundeEventList.get(i);
            kafkaHandler.sendEventToKafka("kunden", kundeEvent.getIdx(), kundeEvent, Collections.emptyMap());
        }

    }


}
