package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

@Repository
@AllArgsConstructor
public class AccidentHibernate {
    private final SessionFactory sf;

    public void findById() {
        Session session = sf.openSession();
        session.beginTransaction();
        Accident accident = session.get(Accident.class, 3);
        System.out.println(accident);
        session.getTransaction().commit();
        session.close();
    }
}