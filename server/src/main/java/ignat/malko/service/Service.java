package ignat.malko.service;

import java.util.List;

public interface Service<T> {

    void persist(T entity);

    void remove(T entity);

    void merge(T entity);

    T findById(Long id);

    List<T> findAll();

}
