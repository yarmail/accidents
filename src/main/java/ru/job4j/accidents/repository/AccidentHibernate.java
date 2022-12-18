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

    public Optional<Accident> findById(int id) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Accident accident = session.get(Accident.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(accident);
        }
    }

    public List<Accident> findAll() {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            List<Accident> accidents = session.createQuery("FROM Accident").getResultList();
            accidents.forEach(System.out::println);
            session.getTransaction().commit();
            return accidents;
        }
    }

    public int save(Accident accident) {
       try (Session session = sf.openSession()) {
           session.beginTransaction();
           session.save(accident);
           session.getTransaction().commit();
           return accident.getId();
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