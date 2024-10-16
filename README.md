# Kafka Streams In Action

### Technische Voraussetzungen

* Java 21
* Spring Boot 3
* Maven
* docker-compose

---

### Lokale Ausführung

### Build

- `mvn clean install`

### Run

1. Im Ordner `/scripts/` `./start-services.sh` ausführen um Kafka, AKHQ, Prometheus und Grafana zu starten.
2. Anlegen der Topics mittels Kafka Console und der beigefügten `./create_topiucs.sh` im Ordner `/scripts/`:
2. In den verzeichnissen `bestellungen-simulator-app`, `monitoring-app` und `kafka-streaming-app` jeweils die Appliktion
   mit java -jar im jeweilgen `target`-Verzeichnis starten.

---

### Monitoring

Für das Monitoring gibt es ein spartanisches Frontend unter [http://localhost:8081](http://localhost:8081)

Zusätzlich gibt es eine weitere Ansicht für die aggregierten Kundenbestelungen als Grafan-Dashboard
unter [http://localhost:3000](http://localhost:3000)

Um Einblich zu erhalten welche Events erzeugt wurden kann dies mit AKHQ [http://localhost:8085](http://localhost:8085)
angesehen werden.

