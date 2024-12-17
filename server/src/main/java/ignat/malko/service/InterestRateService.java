package ignat.malko.service;

import ignat.malko.model.InterestRate;
import ignat.malko.model.enums.AccountType;
import ignat.malko.repository.InterestRateRepository;
import ignat.malko.repository.interfaces.InterestRateDataAccessObject;

import java.util.List;

public class InterestRateService implements Service<InterestRate> {
    private InterestRateDataAccessObject repository = new InterestRateRepository();
    @Override
    public void persist(InterestRate entity) {
        repository.persist(entity);
    }

    @Override
    public void remove(InterestRate entity) {
        repository.remove(entity);
    }

    @Override
    public void merge(InterestRate entity) {
            repository.merge(entity);
    }

    @Override
    public InterestRate findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<InterestRate> findAll() {
        return repository.findAll();
    }

    public InterestRate findByAccountType(AccountType type) {
        return repository.findByAccountType(type);
    }
}
