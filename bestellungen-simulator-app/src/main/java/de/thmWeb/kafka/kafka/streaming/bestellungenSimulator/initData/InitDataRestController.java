package de.thmWeb.kafka.kafka.streaming.bestellungenSimulator.initData;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class InitDataRestController {

    private final ArtikelImporter artikelImporter;
    private final KundenImporter kundenImporter;

    public InitDataRestController(ArtikelImporter artikelImporter, KundenImporter kundenImporter) {
        this.artikelImporter = artikelImporter;
        this.kundenImporter = kundenImporter;
    }


    @GetMapping("/init-artikel/{numberOfArtikel}")
    public String initArtikelData(@PathVariable(value = "numberOfArtikel") final Integer numberOfArtikel) {
        try {
            artikelImporter.importArtikel(numberOfArtikel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(numberOfArtikel);
    }

    @GetMapping("/init-kunden/{numberOfKunden}")
    public String initKundenData(@PathVariable(value = "numberOfKunden") final Integer numberOfKunden) {
        try {
            kundenImporter.importArtikel(numberOfKunden);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(numberOfKunden);
    }

}
