package ignat.malko.service;

import ignat.malko.model.Account;
import ignat.malko.model.enums.AccountType;
import ignat.malko.repository.AccountRepository;
import ignat.malko.repository.interfaces.AccountDataAccessObject;

import java.util.List;

public class AccountService implements Service<Account> {

    private AccountDataAccessObject repository = new AccountRepository();

    @Override
    public void persist(Account entity) {
        repository.persist(entity);
    }

    @Override
    public void remove(Account entity) {
        repository.remove(entity);
    }

    @Override
    public void merge(Account entity) {
        repository.merge(entity);
    }

    @Override
    public Account findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Account> findAll() {
        return repository.findAll();
    }

    public List<Account> findByUserId(Long id) {
        return repository.findByUserId(id);
    }
    public Account findByUserAndType(Long id, AccountType type){
        return repository.findByUserIdAndType(id, type);
    }

    public Account findByNumber(String number) {
        return repository.findByNumber(number);
    }
}
