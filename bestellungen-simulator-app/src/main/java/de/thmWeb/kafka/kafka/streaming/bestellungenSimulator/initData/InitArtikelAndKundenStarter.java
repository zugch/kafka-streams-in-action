package de.thmWeb.kafka.kafka.streaming.bestellungenSimulator.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitArtikelAndKundenStarter implements CommandLineRunner {

    private final ArtikelImporter artikelImporter;
    private final KundenImporter kundenImporter;

    public InitArtikelAndKundenStarter(ArtikelImporter artikelImporter, KundenImporter kundenImporter) {
        this.artikelImporter = artikelImporter;
        this.kundenImporter = kundenImporter;
    }


    @Override
    public void run(String... args) throws Exception {
        artikelImporter.importArtikel(50);
        kundenImporter.importKunden(10);
    }
}
