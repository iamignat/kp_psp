package ignat.malko.repository;

import ignat.malko.model.Account;
import ignat.malko.model.enums.AccountType;
import ignat.malko.repository.interfaces.AccountDataAccessObject;
import ignat.malko.util.HibernateSessionFactory;
import org.hibernate.Session;

import java.util.List;

public class AccountRepository  implements AccountDataAccessObject {
        @Override
        public void persist(Account object) {
            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            session.beginTransaction();
            try {
                session.persist(object);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                System.err.println(e.getMessage());
            }
            finally {
                session.close();
            }
        }

        @Override
        public void merge(Account object) {
            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            session.beginTransaction();
            try {
                session.merge(object);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                System.err.println(e.getMessage());
            }
            finally {
                session.close();
            }
        }

        @Override
        public void remove(Account object) {
            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            session.beginTransaction();
            try {
                session.remove(object);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                System.err.println(e.getMessage());
            } finally {
                session.close();
            }
        }

        @Override
        public Account findById(Long id) {
            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            session.beginTransaction();
            try {
                return session.createQuery("from Account where id = :id", Account.class)
                        .setParameter("id", id)
                        .uniqueResult();
            } catch (Exception e) {
                session.getTransaction().rollback();
                System.err.println(e.getMessage());
            } finally {
                session.close();
            }
            return null;
        }

        @Override
        public List<Account> findAll() {
            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            session.beginTransaction();
            try {
                return session.createQuery("from Account ", Account.class).list();
            } catch (Exception e) {
                session.getTransaction().rollback();
                System.err.println(e.getMessage());
            } finally {
                session.close();
            }
            return null;
        }
        public List<Account> findByUserId(Long id) {
            Session session = HibernateSessionFactory.getSessionFactory().openSession();
            session.beginTransaction();
            try {
                return session.createQuery("from Account where owner.id = :id", Account.class)
                        .setParameter("id", id)
                        .list();
            } catch (Exception e) {
                session.getTransaction().rollback();
            } finally {
                session.close();
            }
            return null;
        }

    @Override
    public Account findByUserIdAndType(Long id, AccountType type) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from Account where owner.id = :id and type = :type", Account.class)
                    .setParameter("id", id)
                    .setParameter("type", type)
                    .uniqueResult();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public Account findByNumber(String number) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            return session.createQuery("from Account where number = :number", Account.class)
                    .setParameter("number", number)
                    .uniqueResult();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;
    }
}
