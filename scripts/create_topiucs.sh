#!/usr/bin/env bash
kafka-topics --create --topic topbestellungen --partitions 1 --replication-factor 1  --bootstrap-server localhost:9092
kafka-topics --create --topic bestellungen --partitions 1 --replication-factor 1  --bootstrap-server localhost:9092
kafka-topics --create --topic kunden --partitions 1 --replication-factor 1  --bootstrap-server localhost:9092
kafka-topics --create --topic bestellungen-aggregiert-pro-kunde --partitions 1 --replication-factor 1  --bootstrap-server localhost:9092
kafka-topics --create --topic artikel --partitions 1 --replication-factor 1  --bootstrap-server localhost:9092
