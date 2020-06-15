package fabiojose.bank.balance;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;

import fabiojose.bank.balance.model.Balance;
import fabiojose.bank.balance.model.StatementEvent;
import io.quarkus.kafka.client.serialization.JsonbSerde;
import lombok.extern.slf4j.Slf4j;

/**
 * The Kafka Streams topology builder
 * 
 * @author fabiojose
 */
@ApplicationScoped
@Slf4j
public class TopologyBuilder {

    @Produces
    public Topology build() {

       // ####
        // Serde for StatementEvent
        final JsonbSerde<StatementEvent> statementEventSerde = 
            new JsonbSerde<>(StatementEvent.class);

        // ####
        // Serde for Balance
        final JsonbSerde<Balance> balanceSerde = 
            new JsonbSerde<>(Balance.class);

        // ####
        // Balance store
        final KeyValueBytesStoreSupplier balanceStore = 
            Stores.persistentKeyValueStore("balances-store");

        final StreamsBuilder builder = new StreamsBuilder();

        // ####
        // Build the stream!
        builder.stream(
            "statements", Consumed.with(Serdes.String(), statementEventSerde)
        )
        .groupByKey()
        .aggregate(Balance::new,
            (accountId, statement, balance) ->{
                log.info("{}", statement);

                // ####
                // Update the current balance 
                return balance.update(statement);
            },
            Materialized.<String, Balance>as(balanceStore)
                .withValueSerde(balanceSerde)
                .withKeySerde(Serdes.String()))
        .toStream()
        .to("balances", Produced.with(Serdes.String(), balanceSerde));

        return builder.build();
    }
}