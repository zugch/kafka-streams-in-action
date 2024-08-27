package de.thmWeb.kafka.kafka.streaming.bestellungenSimulator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RandomBestellungRestController {

    private final BestellungSimulator bestellungSimulator;

    public RandomBestellungRestController(BestellungSimulator bestellungSimulator) {
        this.bestellungSimulator = bestellungSimulator;
    }

    @GetMapping("/bestellungen/{anzahl}")
    public String addAnzahlBestellungen(@PathVariable(value = "anzahl") final Integer anzahl) {
        bestellungSimulator.simulateBestellungen(anzahl);

        return "anzahl: " + anzahl;
    }
}
