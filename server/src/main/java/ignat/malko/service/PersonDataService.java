package ignat.malko.service;

import ignat.malko.model.PersonData;
import ignat.malko.repository.interfaces.DataAccessObject;
import ignat.malko.repository.PersonDataRepository;
import ignat.malko.repository.interfaces.PersonDataAccessObject;

import java.util.List;

public class PersonDataService implements Service<PersonData> {

    private PersonDataAccessObject repository = new PersonDataRepository();

    @Override
    public void persist(PersonData entity) {
        repository.persist(entity);
    }

    @Override
    public void remove(PersonData entity) {
        repository.persist(entity);
    }

    @Override
    public void merge(PersonData entity) {
        repository.merge(entity);
    }

    @Override
    public PersonData findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<PersonData> findAll() {
        return repository.findAll();
    }

    public PersonData findByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }
}
