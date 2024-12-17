package ignat.malko.util;

import ignat.malko.model.*;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
    @Getter
    private static SessionFactory sessionFactory;
    static {
        sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(PersonData.class)
                .addAnnotatedClass(Transaction.class)
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(InterestRate.class)
                .buildSessionFactory();
    }

}