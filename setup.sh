#!/bin/bash

# To setup the test env

kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 7 \
  --topic 'accounts' \
  --config 'cleanup.policy=compact' \
  --config 'segment.ms=86400000' \
  --config 'message.timestamp.type=LogAppendTime'

#kafka-console-producer.sh \
#  --broker-list localhost:9092 \
#  --topic 'accounts' \
#  --property "parse.key=true" \
#  --property "key.separator=|" < payload/accounts.txt

kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 7 \
  --topic 'transactions' \
  --config 'retention.ms=-1' \
  --config 'message.timestamp.type=LogAppendTime'

#kafka-console-producer.sh \
#  --broker-list localhost:9092 \
#  --topic 'transactions' \
#  --property "parse.key=true" \
#  --property "key.separator=|" < payload/transactions.txt

kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 7 \
  --topic 'statements' \
  --config 'retention.ms=-1' \
  --config 'message.timestamp.type=LogAppendTime'

kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 7 \
  --topic 'balances' \
  --config 'segment.ms=60000' \
  --config 'cleanup.policy=compact' \
  --config 'message.timestamp.type=LogAppendTime' \
  --config 'delete.retention.ms=180000' \
  --config 'max.compaction.lag.ms=60000' \
  --config 'min.cleanable.dirty.ratio=0.1'
  
  kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 7 \
  --topic 'balances_assert' \
  --config 'retention.ms=-1' \
  --config 'segment.ms=180000' \
  --config 'cleanup.policy=compact' \
  --config 'message.timestamp.type=LogAppendTime' \
  --config 'delete.retention.ms=180000' \
  --config 'max.compaction.lag.ms=60000'