package ignat.malko.repository;

import ignat.malko.model.InterestRate;
import ignat.malko.model.enums.AccountType;
import ignat.malko.repository.interfaces.InterestRateDataAccessObject;
import ignat.malko.util.HibernateSessionFactory;
import org.hibernate.Session;

import java.util.List;

public class InterestRateRepository implements InterestRateDataAccessObject {

    @Override
    public void persist(InterestRate object) {
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
    public void merge(InterestRate object) {
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
    public void remove(InterestRate object) {
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
    public InterestRate findById(Long id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from InterestRate where id = :id", InterestRate .class)
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
    public List<InterestRate > findAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from InterestRate ", InterestRate .class).list();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public InterestRate findByAccountType(AccountType type) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from InterestRate where type = :type", InterestRate .class)
                    .setParameter("type", type)
                    .uniqueResult();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }
}
