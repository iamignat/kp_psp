package ignat.malko.service;

import ignat.malko.model.Account;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;
import ignat.malko.repository.TransactionRepository;
import ignat.malko.repository.interfaces.TransactionDataAccessObject;

import java.util.List;

public class TransactionService implements Service<Transaction> {

    private TransactionDataAccessObject repository = new TransactionRepository();

    @Override
    public void persist(Transaction entity) {
        repository.persist(entity);
    }

    @Override
    public void remove(Transaction entity) {
        repository.remove(entity);
    }

    @Override
    public void merge(Transaction entity) {
        repository.merge(entity);
    }

    @Override
    public Transaction findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public List<Transaction> findByAccount(Account account) {
        return repository.findByAccount(account);
    }

}
