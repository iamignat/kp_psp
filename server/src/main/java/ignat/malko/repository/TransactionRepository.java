package ignat.malko.repository;

import ignat.malko.model.Account;
import ignat.malko.model.Transaction;
import ignat.malko.model.User;
import ignat.malko.model.enums.AccountType;
import ignat.malko.repository.interfaces.DataAccessObject;
import ignat.malko.repository.interfaces.TransactionDataAccessObject;
import ignat.malko.util.HibernateSessionFactory;
import org.hibernate.Session;

import java.util.List;

public class TransactionRepository implements TransactionDataAccessObject {
    @Override
    public void persist(Transaction object) {
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
    public void merge(Transaction object) {
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
    public void remove(Transaction object) {
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
    public Transaction findById(Long id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from Transaction where id = :id", Transaction.class)
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
    public List<Transaction> findAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from Transaction ", Transaction.class).list();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public List<Transaction> findByAccount(Account account) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from Transaction where receiver = :account or sender = :account", Transaction.class)
                    .setParameter("account", account)
                    .list();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }
}
