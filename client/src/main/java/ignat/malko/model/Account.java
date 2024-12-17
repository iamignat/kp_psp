package ignat.malko.model;

import ignat.malko.model.enums.AccountStatus;
import ignat.malko.model.enums.AccountType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account implements Serializable {
    private Long id;
    private double balance;
    private String number;
    private AccountType type;
    private AccountStatus status;
    private LocalDate created;
    private User owner;
}
