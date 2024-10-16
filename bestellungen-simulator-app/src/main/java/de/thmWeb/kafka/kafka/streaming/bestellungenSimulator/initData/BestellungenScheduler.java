package de.thmWeb.kafka.kafka.streaming.bestellungenSimulator.initData;

import de.thmWeb.kafka.kafka.streaming.bestellungenSimulator.BestellungSimulator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class BestellungenScheduler {

    private final BestellungSimulator bestellungSimulator;
    private final ArtikelImporter artikelImporter;
    private final KundenImporter kundenImporter;
    private boolean isInitialized = false;


    public BestellungenScheduler(BestellungSimulator bestellungSimulator, ArtikelImporter artikelImporter, KundenImporter kundenImporter) {
        this.bestellungSimulator = bestellungSimulator;
        this.artikelImporter = artikelImporter;
        this.kundenImporter = kundenImporter;
    }


    @Scheduled(initialDelay = 10000, fixedRate = 1000)
    public void simulateBestellungen() {
        if (!isInitialized) {
            try {
                log.info("import Artikel and Kunden");
                artikelImporter.importArtikel(50);
                kundenImporter.importKunden(10);
                isInitialized = true;
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        bestellungSimulator.simulateBestellungen(1);
    }
}
