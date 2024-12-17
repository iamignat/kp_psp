package ignat.malko.model;

import ignat.malko.model.enums.AccountStatus;
import ignat.malko.model.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double balance;
    private String number;
    private AccountType type;
    private AccountStatus status;
    private LocalDate created;

    @ManyToOne
    @JoinColumn(name = "rate_id")
    private InterestRate interestRate;


    @ManyToOne()
    @JoinColumn(name = "owner_id")
    User owner;
}
