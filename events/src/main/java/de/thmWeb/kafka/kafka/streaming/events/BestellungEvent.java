package de.thmWeb.kafka.kafka.streaming.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BestellungEvent {

    private String idx;
    private String kundenIdx;
    private List<BestellPositionEvent> bestellPositionen = new ArrayList<>();
    private Integer gesamtSumme = 0;
}
