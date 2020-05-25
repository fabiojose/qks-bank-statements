# Bank Statements

This project uses Quarkus and Kafka Streams to show how to process 
[bank statements](https://en.wikipedia.org/wiki/Bank_statement).

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/.

## Requirements

- Java 11
- Kafka

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./gradlew quarkusDev
```

## Packaging and running the application

The application can be packaged using `./gradlew quarkusBuild`.
It produces the `kafka-streams-quarkus-1.0-SNAPSHOT-runner.jar` file in the `build` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/lib` directory.

The application is now runnable using `java -jar build/kafka-streams-quarkus-1.0-SNAPSHOT-runner.jar`.

If you want to build an _über-jar_, just add the `--uber-jar` option to the command line:
```
./gradlew quarkusBuild --uber-jar
```

## Creating a native executable

You can create a native executable using: `./gradlew build -Dquarkus.package.type=native`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./build/kafka-streams-quarkus-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling#building-a-native-executable.

## Apache Kafka

Here is how to create the topics and how produce events.

### `accounts`

This is the topic for account created events, with `7` partitions.

```bash
kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 7 \
  --topic 'accounts' \
  --config 'cleanup.policy=compact' \
  --config 'segment.ms=86400000' 
```

Produce some account events:

```bash
kafka-console-producer.sh \
  --broker-list localhost:9092 \
  --topic 'accounts' \
  --property "parse.key=true" \
  --property "key.separator=|" < payload/accounts.txt
```

### `transactions`

This is the topic for transaction events, with `7` partitions too.

```bash
kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 7 \
  --topic 'transactions' \
  --config 'retention.ms=-1' 
```

Then, produce some transaction events:

```bash
kafka-console-producer.sh \
  --broker-list localhost:9092 \
  --topic 'transactions' \
  --property "parse.key=true" \
  --property "key.separator=|" < payload/transactions.txt
```

### `statements`

This is the topic with statements.

```bash
kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 7 \
  --topic 'statements' \
  --config 'retention.ms=-1' 
```

Consume the `statements` to see the things happening

```bash
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic 'statements' \
  --property print.key=true \
  --property print.timestamp=true \
  --from-beginning
```

## Run the uber jar

```bash
java \
  -Dquarkus.kafka-streams.bootstrap-servers=localhost:9092 \
  -Dquarkus.kafka-streams.application-id=statements \
  -Dquarkus.kafka-streams.application-server=localhost:8080 \
  -Dquarkus.kafka-streams.topics=accounts,transactions,statements \
  -jar build/kafka-streams-quarkus-1.0-SNAPSHOT-runner.jar
```

## Run the native executable

```bash
build/kafka-streams-quarkus-1.0-SNAPSHOT-runner \
  -Dquarkus.kafka-streams.bootstrap-servers=localhost:9092 \
  -Dquarkus.kafka-streams.application-id=statements \
  -Dquarkus.kafka-streams.application-server=localhost:8080 \
  -Dquarkus.kafka-streams.topics=accounts,transactions,statements
```

# pass-through options
kafka-streams.cache.max.bytes.buffering=10240
kafka-streams.commit.interval.ms=1000
kafka-streams.metadata.max.age.ms=500
kafka-streams.auto.offset.reset=earliest
kafka-streams.metrics.recording.level=DEBUG