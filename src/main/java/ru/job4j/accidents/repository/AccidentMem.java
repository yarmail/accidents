package ru.job4j.accidents.repository;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;

import java.util.Collection;
import java.util.HashMap;

/**
 * хранилище инцидентов, Здесь мы не работаем c postgresql.
 * Работа с базой будет дальше.
 */
@Repository
public class AccidentMem {
    private final HashMap<Integer, Accident> accidents = new HashMap<>();

    public AccidentMem() {
        accidents.put(1, new Accident(1, "name1", "text1", "address1"));
        accidents.put(2, new Accident(2, "name2", "text2", "address2"));
        accidents.put(3, new Accident(3, "name3", "text3", "address3"));
    }

    public Collection<Accident> findAll() {
        return accidents.values();
    }
}