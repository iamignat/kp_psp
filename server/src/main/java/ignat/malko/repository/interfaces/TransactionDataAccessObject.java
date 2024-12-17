package ignat.malko.repository.interfaces;

import ignat.malko.model.Account;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;

import java.util.List;

public interface TransactionDataAccessObject  extends DataAccessObject<Transaction> {
    List<Transaction> findByAccount(Account account);
}
