package de.thmWeb.kafka.kafka.streaming.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ArtikelEvent {

    private String idx;
    private String artikelName;
    private String kategorie;

    private Integer preis;
}
