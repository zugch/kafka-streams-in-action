package de.thmWeb.kafka.kafka.streaming.bestellungenSimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BestellungenSimulatorApplication {

    public static void main(String[] args) {

        SpringApplication.run(BestellungenSimulatorApplication.class, args);
    }

}
