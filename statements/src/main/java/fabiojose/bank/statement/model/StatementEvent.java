package fabiojose.bank.statement.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

/**
 * Flatten bank statement event
 * 
 * @author fabiojose
 */
@Data
@RegisterForReflection
public class StatementEvent {

    private UUID accountIdentifier;
    private UUID accountOwnerIdentifier;
    private String accountOwnerName;

    private UUID transactionIdentifier;
    private ZonedDateTime transactionTime;
    private String transactionTitle;
    private String transactionInfo;

    private double transactionAmount;

}