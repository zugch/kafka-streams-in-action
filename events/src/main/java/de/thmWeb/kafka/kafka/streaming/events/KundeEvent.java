package de.thmWeb.kafka.kafka.streaming.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class KundeEvent {

    private String idx;
    private String name;
    private String email;
    private Integer gesammtBestellSumme;
}
