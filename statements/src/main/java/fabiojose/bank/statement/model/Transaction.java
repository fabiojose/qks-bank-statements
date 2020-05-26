package fabiojose.bank.statement.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

/**
 * A simple transaction that changes the account's balance
 * 
 * @author fabiojose
 */
@Data
@RegisterForReflection
public class Transaction {
    
    private UUID accountIdentifier;
   
    private UUID identifier;
    private ZonedDateTime time;

    private String title;
    private String info;

    /**
     * Positive values=deposits
     * Negative values=withdraws
     */
    private double amount;

}