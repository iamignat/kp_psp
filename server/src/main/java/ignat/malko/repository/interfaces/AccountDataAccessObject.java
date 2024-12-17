package ignat.malko.repository.interfaces;

import ignat.malko.model.Account;
import ignat.malko.model.enums.AccountType;

import java.util.List;

public interface AccountDataAccessObject extends DataAccessObject<Account> {
    public List<Account> findByUserId(Long id);
    public Account findByUserIdAndType(Long id, AccountType type);

    Account findByNumber(String number);
}
