package ignat.malko.repository.interfaces;

import java.util.List;

public interface DataAccessObject<T> {
        void persist(T object);
        void merge(T object);
        void remove(T object);
        T findById(Long id);
        List<T> findAll();
}
