package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accidents.model.Accident;

import java.util.Collection;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccidentHibernate {
    private final SessionFactory sf;

    @Transactional(readOnly = true)
    public Optional<Accident> findById(int id) {
        try (Session session = sf.openSession()) {
            Accident accident = session.get(Accident.class, id);
            return Optional.ofNullable(accident);
        }
    }

    @Transactional(readOnly = true)
    public Collection<Accident> findAll() {
        try (Session session = sf.openSession()) {
        Collection<Accident> accidents =
                session.createQuery("FROM Accident").getResultList();
        return accidents;
        }
    }

    @Transactional
    public int save(Accident accident) {
       try (Session session = sf.openSession()) {
           session.save(accident);
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