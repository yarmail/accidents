package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccidentHibernate {
    private final SessionFactory sf;
    private static final String ALL_ACCIDENTS = """
        SELECT a FROM Accident a
        JOIN FETCH a.type
        JOIN FETCH a.rules 
    """;

    private static final String ONE_ACCIDENT = """
        SELECT a FROM Accident a
        JOIN FETCH a.type
        JOIN FETCH a.rules
        WHERE a.id = :fId 
    """;

    public Optional<Accident> findById(int id) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Optional<Accident> accident = session.createQuery(ONE_ACCIDENT)
                .setParameter("fId", id).uniqueResultOptional();
            session.getTransaction().commit();
            return accident;
        }
    }

    public List<Accident> findAll() {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            List<Accident> accidents =
                    session.createQuery(ALL_ACCIDENTS).getResultList();
            session.getTransaction().commit();
            return accidents;
        }
    }

    public int save(Accident accident) {
       try (Session session = sf.openSession()) {
           session.beginTransaction();
           int id = (int) session.save(accident);
           session.getTransaction().commit();
           return id;
       }
    }

    public void replace(Accident newAccident) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.update(newAccident);
            session.getTransaction().commit();
        }
    }
}