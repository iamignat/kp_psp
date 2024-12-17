package ignat.malko.service;

import ignat.malko.model.User;
import ignat.malko.repository.UserRepository;
import ignat.malko.repository.interfaces.UserDataAccessObject;

import java.util.List;

public class UserService implements Service<User> {

    UserDataAccessObject repository = new UserRepository();

    @Override
    public void persist(User entity) {
        repository.persist(entity);
    }

    @Override
    public void remove(User entity) {
        repository.remove(entity);
    }

    @Override
    public void merge(User entity) {
        repository.merge(entity);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }
}
