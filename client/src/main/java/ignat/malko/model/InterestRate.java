package ignat.malko.model;

import ignat.malko.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestRate implements Serializable {
    private Long id;
    private AccountType type;
    private double value;
}
