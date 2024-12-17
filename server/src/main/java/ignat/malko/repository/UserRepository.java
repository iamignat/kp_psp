package ignat.malko.repository;

import ignat.malko.model.User;
import ignat.malko.repository.interfaces.UserDataAccessObject;
import ignat.malko.util.HibernateSessionFactory;
import org.hibernate.Session;

import java.util.List;

public class UserRepository implements UserDataAccessObject {

    @Override
    public void persist(User object) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.persist(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            session.getTransaction().rollback();
        }
        finally {
            session.close();
        }
    }

    @Override
    public void merge(User object) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.merge(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            session.getTransaction().rollback();
        }
        finally {
            session.close();
        }
    }

    @Override
    public void remove(User object) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.remove(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public User findById(Long id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from User where id = :id", User.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }
}