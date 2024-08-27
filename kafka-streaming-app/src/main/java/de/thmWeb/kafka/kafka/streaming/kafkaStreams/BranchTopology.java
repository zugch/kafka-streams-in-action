package de.thmWeb.kafka.kafka.streaming.kafkaStreams;

import de.thmWeb.kafka.kafka.streaming.kafkaStreams.serde.SerdeFactory;
import de.thmWeb.kafka.kafka.streaming.events.ArtikelEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BranchTopology implements ExampleTopology {


    @Override
    public void createTopology(StreamsBuilder builder) {

        final KStream<String, ArtikelEvent> artikelEvents =
                builder.stream("artikel", Consumed.with(Serdes.String(), SerdeFactory.serdeForArtikelEvent()));


        final Map<String, KStream<String, ArtikelEvent>> branchedArtikelEventMap = artikelEvents.split(Named.as("branch-"))
                .branch((key, val) -> val.getKategorie().equals("Tablets"), Branched.as("tablets"))
                .branch((key, val) -> val.getKategorie().equals("Smartphones"), Branched.as("smartphones"))
                .branch((key, val) -> val.getKategorie().equals("Laptops"), Branched.as("laptops"))
                .branch((key, val) -> val.getKategorie().equals("Audio"), Branched.as("audio"))
                .branch((key, val) -> val.getKategorie().equals("Televisions"), Branched.as("televisions"))
                .branch((key, val) -> val.getKategorie().equals("Gaming"), Branched.as("gaming"))
                .branch((key, val) -> val.getKategorie().equals("Wearables"), Branched.as("wearables"))
                .branch((key, val) -> val.getKategorie().equals("Cameras"), Branched.as("cameras"))
                .branch((key, val) -> val.getKategorie().equals("Accessories"), Branched.as("accessories"))
                .branch((key, val) -> val.getKategorie().equals("Networking"), Branched.as("networking"))
                .defaultBranch();

        branchedArtikelEventMap.get("branch-laptops").to("artikel-laptops", Produced.with(Serdes.String(), SerdeFactory.serdeForArtikelEvent()));
        branchedArtikelEventMap.get("branch-tablets").to("artikel-tablets", Produced.with(Serdes.String(), SerdeFactory.serdeForArtikelEvent()));
        branchedArtikelEventMap.get("branch-smartphones").to("artikel-smartphones", Produced.with(Serdes.String(), SerdeFactory.serdeForArtikelEvent()));
        branchedArtikelEventMap.get("branch-audio").to("artikel-audio", Produced.with(Serdes.String(), SerdeFactory.serdeForArtikelEvent()));
        branchedArtikelEventMap.get("branch-televisions").to("artikel-televisions", Produced.with(Serdes.String(), SerdeFactory.serdeForArtikelEvent()));
        branchedArtikelEventMap.get("branch-gaming").to("artikel-gaming", Produced.with(Serdes.String(), SerdeFactory.serdeForArtikelEvent()));
        branchedArtikelEventMap.get("branch-wearables").to("artikel-wearables", Produced.with(Serdes.String(), SerdeFactory.serdeForArtikelEvent()));
        branchedArtikelEventMap.get("branch-cameras").to("artikel-cameras", Produced.with(Serdes.String(), SerdeFactory.serdeForArtikelEvent()));
        branchedArtikelEventMap.get("branch-accessories").to("artikel-accessories", Produced.with(Serdes.String(), SerdeFactory.serdeForArtikelEvent()));
        branchedArtikelEventMap.get("branch-networking").to("artikel-networking", Produced.with(Serdes.String(), SerdeFactory.serdeForArtikelEvent()));

    }
}
