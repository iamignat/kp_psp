package ignat.malko.repository;

import ignat.malko.model.PersonData;
import ignat.malko.repository.interfaces.DataAccessObject;
import ignat.malko.repository.interfaces.PersonDataAccessObject;
import ignat.malko.util.HibernateSessionFactory;
import org.hibernate.Session;

import java.util.List;

public class PersonDataRepository implements PersonDataAccessObject {

    @Override
    public void persist(PersonData object) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.persist(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

    }

    @Override
    public void merge(PersonData object) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.merge(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void remove(PersonData object) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.remove(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public PersonData findById(Long id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from PersonData where id = :id", PersonData.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public List<PersonData> findAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from PersonData", PersonData.class).list();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public PersonData findByPhoneNumber(String phoneNumber) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from PersonData where phoneNumber = :phoneNumber", PersonData.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .uniqueResult();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }
}
