package de.thmWeb.kafka.kafka.streaming.bestellungenSimulator.initData;

import de.thmWeb.kafka.kafka.streaming.bestellungenSimulator.BestellungSimulator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BestellungenScheduler {

    private final BestellungSimulator bestellungSimulator;

    public BestellungenScheduler(BestellungSimulator bestellungSimulator) {
        this.bestellungSimulator = bestellungSimulator;
    }


    @Scheduled(initialDelay = 20000, fixedRate = 1000)
    public void simulateBestellungen() {
        bestellungSimulator.simulateBestellungen(1);
    }
}
