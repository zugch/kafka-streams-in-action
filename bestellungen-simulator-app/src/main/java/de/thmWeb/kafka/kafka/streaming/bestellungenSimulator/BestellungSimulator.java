package de.thmWeb.kafka.kafka.streaming.bestellungenSimulator;

import de.thmWeb.kafka.kafka.streaming.events.ArtikelEvent;
import de.thmWeb.kafka.kafka.streaming.events.BestellPositionEvent;
import de.thmWeb.kafka.kafka.streaming.events.BestellungEvent;
import de.thmWeb.kafka.kafka.streaming.events.KundeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class BestellungSimulator {

    private final KafkaHandler kafkaHandler;


    private final List<ArtikelEvent> artikelEventList = new ArrayList<>();
    private final List<KundeEvent> kundeEventList = new ArrayList<>();

    public BestellungSimulator(KafkaHandler kafkaHandler) {
        this.kafkaHandler = kafkaHandler;
    }


    @KafkaListener(topics = "bestellungen", containerFactory = "bestellungenEventKafkaListenerContainerBatchFactory")
    public void listenToBestellungen(@Payload final BestellungEvent bestellungEvent) {
        log.info("Received bestellungEvent event: {}", bestellungEvent);
    }

    @KafkaListener(topics = "artikel", containerFactory = "artikelEventKafkaListenerContainerBatchFactory")
    public void listenToArtikel(@Payload final ArtikelEvent artikelEvent) {
        artikelEventList.add(artikelEvent);
    }

    @KafkaListener(topics = "kunden", containerFactory = "kundeEventKafkaListenerContainerBatchFactory")
    public void listenToKunden(@Payload final KundeEvent kundeEvent) {
        kundeEventList.add(kundeEvent);
    }


    public void simulateBestellungen(Integer anzahlBestellungen) {
        log.info("sende {} bestellungen", anzahlBestellungen);
        for (int i = 1; i <= anzahlBestellungen; i++) {
            BestellungEvent bestellungEvent = erstelleBestellung();
            kafkaHandler.sendEventToKafka("bestellungen", bestellungEvent.getIdx(), bestellungEvent, Collections.emptyMap());
        }

        log.info("DONE");

    }

    private BestellungEvent erstelleBestellung() {
        Random random = new Random();
        int artikelIndex = random.nextInt(0, artikelEventList.size());
        int kundenIndex = random.nextInt(0, kundeEventList.size());
        ArtikelEvent artikelEvent = artikelEventList.get(artikelIndex);
        KundeEvent kundeEvent = kundeEventList.get(kundenIndex);
        BestellungEvent bestellungEvent = BestellungEvent.builder()
                .idx(UUID.randomUUID().toString())
                .kundenIdx(kundeEvent.getIdx())
                .bestellPositionen(new ArrayList<>())
                .gesamtSumme(artikelEvent.getPreis())
                .build();
        bestellungEvent.getBestellPositionen().add(BestellPositionEvent.builder()
                .idx(UUID.randomUUID().toString())
                .anzahlArtikel(1)
                .artikelEvent(artikelEvent)
                .build());

        return bestellungEvent;

    }

}
