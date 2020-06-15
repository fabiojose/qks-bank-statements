package fabiojose.bank.balance.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

/**
 * @author fabiojose
 */
@Data
@RegisterForReflection
public class Balance {
    
    private UUID accountIdentifier;
    private UUID accountOwnerIdentifier;
    private String accountOwnerName;

    private ZonedDateTime time;
    private BigDecimal balance = new BigDecimal("0.0000");

    public Balance update(StatementEvent statement) {

        setAccountIdentifier(statement.getAccountIdentifier());
        setAccountOwnerIdentifier(statement.getAccountOwnerIdentifier());
        setAccountOwnerName(statement.getAccountOwnerName());

        setTime(ZonedDateTime.now());

        // Update the current balance
        BigDecimal availbleBalance = 
        getBalance()
            .add(BigDecimal.valueOf(statement.getTransactionAmount()));

        setBalance(availbleBalance);

        return this;
    }

}