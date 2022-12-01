package ru.job4j.accidents.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccidentTypeMem {
    private final Map<Integer, AccidentType> accidentType = new ConcurrentHashMap<>();

    public AccidentTypeMem() {
        accidentType.put(1, new AccidentType(1, "Две машины"));
        accidentType.put(2, new AccidentType(2, "Машина и человек"));
        accidentType.put(3, new AccidentType(3, "Машина и велосипед"));
    }

    public Collection<AccidentType> findAll() {
        return accidentType.values();
    }

    public AccidentType findById(int id) {
        return accidentType.get(id);
    }
}
