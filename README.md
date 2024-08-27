# Kafka Streams In Action


### Technische Voraussetzungen
* Java 21
* Spring Boot 3
* Maven
* docker-compose

---

### Lokale Ausführung


### Build
-  `mvn clean install`

### Run

1. Im Ordner `/scripts/` `./start-services.sh` ausführen um Kafka, AKHQ, Prometheus und Grafana zu starten.
2. In den verzeichnissen `bestellungen-simulator-app`, `monitoring-app` und `kafka-streaming-app` jeweils die Appliktion mit java -jar im jeweilgen `target`-Verzeichnis starten.
3. 10 Kunden anlegen mit `curl http://localhost:8080/init-kunden/10`
4. 50 Artikel anlegen mit `curl http://localhost:8080/init-artikel/50`
5. 500 Bestellungen anlegen mit `curl http://localhost:8080/bestellungen/500`
---


### Monitoring

Für die aggregierten Userbestellungen gibt es im Grafana ein Dashboard unter [http://localhost:3000](http://localhost:3000)

Um Einblich zu erhalten welche Events erzeugt wurden kann dies mit AKHQ [http://localhost:8085](http://localhost:8085) angesehen werden.

