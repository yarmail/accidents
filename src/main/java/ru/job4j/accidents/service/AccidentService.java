package ru.job4j.accidents.service;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.repository.AccidentMem;
import java.util.Collection;

@Service
public class AccidentService {
    private final AccidentMem accidentMem;

    public AccidentService(AccidentMem accidentMem) {
        this.accidentMem = accidentMem;
    }

    public Collection<Accident> findAll() {
        return accidentMem.findAll();
    }

    public void add(Accident accident) {
        accidentMem.add(accident);
    }

    public void replace(Accident accident) {
        accidentMem.replace(accident);
    }

    public Accident findById(int id) {
        return accidentMem.findById(id);
    }
}