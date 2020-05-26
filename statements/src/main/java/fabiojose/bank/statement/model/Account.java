package fabiojose.bank.statement.model;

import java.util.UUID;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

/**
 * Bank account
 * 
 * @author fabiojose
 */
@Data
@RegisterForReflection
public class Account {
    
    private UUID identifier;
    private Person ownedBy;

}