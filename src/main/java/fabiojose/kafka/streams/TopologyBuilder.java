package fabiojose.kafka.streams;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Produced;

import fabiojose.kafka.streams.model.Account;
import fabiojose.kafka.streams.model.StatementEvent;
import fabiojose.kafka.streams.model.Transaction;
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
        // Serde for Account
        JsonbSerde<Account> accountSerde = 
            new JsonbSerde<>(Account.class);

        // #### 
        // Serde for Transaction
        JsonbSerde<Transaction> transactionSerde = 
            new JsonbSerde<>(Transaction.class);

        // ####
        // Serde for StatementEvent
        JsonbSerde<StatementEvent> statementEventSerde = 
            new JsonbSerde<>(StatementEvent.class);

        final StreamsBuilder builder = new StreamsBuilder();

        // ####
        // Table with accounts
        GlobalKTable<String, Account> accounts = 
            builder.globalTable("accounts", 
                Consumed.with(Serdes.String(), accountSerde));

        // ####
        // Build the stream!
        builder.stream(
            "transactions", Consumed.with(Serdes.String(), transactionSerde)
        )
        .join(accounts,
            (accountId, tx) -> accountId,
            (tx, account) -> {

                log.info("Transaction {}", tx);
                log.info("Account     {}", account);

                StatementEvent statement = new StatementEvent();

                statement.setAccountIdentifier(account.getIdentifier());
                statement.setAccountOwnerIdentifier(
                    account.getOwnedBy().getIdentifier());
                statement.setAccountOwnerName(
                    account.getOwnedBy().getGivenName());

                statement.setTransactionIdentifier(tx.getIdentifier());
                statement.setTransactionTime(tx.getTime());
                statement.setTransactionTitle(tx.getTitle());
                statement.setTransactionInfo(tx.getInfo());
                statement.setTransactionAmount(tx.getAmount());

                return statement;
            }
        )
        .to("statements", Produced.with(Serdes.String(), statementEventSerde));

        return builder.build();
    }
}