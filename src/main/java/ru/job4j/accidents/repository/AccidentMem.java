package ru.job4j.accidents.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * хранилище инцидентов, Здесь мы не работаем c postgresql.
 * Работа с базой будет дальше.
 */
@Repository
public class AccidentMem {
    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    public AccidentMem() {
        accidents.put(id.incrementAndGet(), new Accident(id.get(), "name1", "text1", "address1"));
        accidents.put(id.incrementAndGet(), new Accident(id.get(), "name2", "text2", "address2"));
        accidents.put(id.incrementAndGet(), new Accident(id.get(), "name3", "text3", "address3"));
    }

    public Collection<Accident> findAll() {
        return accidents.values();
    }

    public Accident findById(int id) {
        return accidents.get(id);
    }

    public void add(Accident accident) {
        accident.setId(id.incrementAndGet());
        accidents.putIfAbsent(accident.getId(), accident);
    }

    public void replace(Accident accident) {
        accidents.put(accident.getId(), accident);
    }
}