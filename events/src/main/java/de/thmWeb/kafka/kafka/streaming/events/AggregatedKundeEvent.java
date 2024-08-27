package de.thmWeb.kafka.kafka.streaming.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedKundeEvent {

    private String kundenIdx;
    private KundeEvent kunde;
    private Integer gesamtSumme = 0;

}
