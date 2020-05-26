package fabiojose.bank.balance.model;

import java.time.LocalDate;
import java.util.UUID;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

/**
 * Person using the definitions at https://schema.org/Person
 * 
 * @author fabiojose
 */
@Data
@RegisterForReflection
public class Person {
    
    private UUID identifier;

    private String givenName;
    private String additionalName;
    private String familyName;

    private LocalDate birthDate;

}