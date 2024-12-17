package ignat.malko.repository.interfaces;

import ignat.malko.model.InterestRate;
import ignat.malko.model.enums.AccountType;

public interface InterestRateDataAccessObject extends DataAccessObject<InterestRate> {
    InterestRate findByAccountType(AccountType type);
}
