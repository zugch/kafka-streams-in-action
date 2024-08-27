package de.thmWeb.kafka.kafka.streaming.bestellungenSimulator.initData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import de.thmWeb.kafka.kafka.streaming.bestellungenSimulator.KafkaHandler;
import de.thmWeb.kafka.kafka.streaming.events.ArtikelEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@Component
public class ArtikelImporter {

    private final KafkaHandler kafkaHandler;

    public ArtikelImporter(KafkaHandler kafkaHandler) {
        this.kafkaHandler = kafkaHandler;
    }


    public void importArtikel(final Integer numberOfArtikel) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.readerForListOf(ArtikelEvent.class);

        URL resource = this.getClass().getClassLoader().getResource("elektronik_katalog.json");
        List<ArtikelEvent> artikelEventList = objectReader.readValue(resource);
        for (int i = 0; i < numberOfArtikel; i++) {
            ArtikelEvent artikelEvent = artikelEventList.get(i);
            kafkaHandler.sendEventToKafka("artikel", artikelEvent.getIdx(), artikelEvent, Collections.emptyMap());
        }

    }

}
