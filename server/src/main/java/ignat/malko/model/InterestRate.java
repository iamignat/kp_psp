package ignat.malko.model;

import ignat.malko.model.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "interest_rates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private AccountType type;
    private double value;
}
