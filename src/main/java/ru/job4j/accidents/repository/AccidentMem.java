package ru.job4j.accidents.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem {
    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    public AccidentMem() {
        Rule rule1 = new Rule(1, "Статья1");
        Rule rule2 = new Rule(2, "Статья2");
        Rule rule3 = new Rule(3, "Статья3");
        Set<Rule> rules1 = Set.of(rule1);
        Set<Rule> rules2 = Set.of(rule1, rule2);
        Set<Rule> rules3 = Set.of(rule2, rule3);

        AccidentType accidentType1 = new AccidentType(1, "Две машины");
        AccidentType accidentType2 = new AccidentType(2, "Машина и человек");
        AccidentType accidentType3 = new AccidentType(3, "Машина и велосипед");

        Accident accident1 = new Accident(1, "name1", "text1", "address1", accidentType1, rules1);
        Accident accident2 = new Accident(2, "name2", "text2", "address2", accidentType2, rules2);
        Accident accident3 = new Accident(3, "name3", "text3", "address3", accidentType3, rules3);
        accidents.put(id.incrementAndGet(), accident1);
        accidents.put(id.incrementAndGet(), accident2);
        accidents.put(id.incrementAndGet(), accident3);
    }

    public Collection<Accident> findAll() {
        return accidents.values();
    }

    public Accident findById(int id) {
        return accidents.get(id);
    }

    public void save(Accident accident) {
        accident.setId(id.incrementAndGet());
        accidents.putIfAbsent(accident.getId(), accident);
    }

    public void replace(Accident accident) {
        accidents.put(accident.getId(), accident);
    }
}