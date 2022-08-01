#!/bin/sh
set -x
# waiting for kafka to be reachable
kafka-topics --bootstrap-server "${KAFKA_BROKER}" --list

echo 'Creating kafka topics'
kafka-topics --bootstrap-server "${KAFKA_BROKER}" --create --topic "${LINES_TOPIC_NAME}" --replication-factor 1 --partitions 1
echo 'Successfully created the following topics:'
kafka-topics --bootstrap-server "${KAFKA_BROKER}" --list