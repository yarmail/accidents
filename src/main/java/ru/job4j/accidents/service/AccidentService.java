package ru.job4j.accidents.service;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.AccidentMem;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class AccidentService {
    private final AccidentMem accidentMem;
    private final RuleService ruleService;
    private final AccidentTypeService accidentTypeService;

    public AccidentService(AccidentMem accidentMem,
                           RuleService ruleService,
                           AccidentTypeService accidentTypeService) {
        this.accidentMem = accidentMem;
        this.ruleService = ruleService;
        this.accidentTypeService = accidentTypeService;
    }

    public Collection<Accident> findAll() {
        return accidentMem.findAll();
    }

    /**
     * rIds - список статей
     * typeId - id типа происшествия
     */
    public void add(Accident accident, String[] rIds, int typeId) {
        Set<Rule> rules = new HashSet<>();
        for (String statId : rIds) {
            rules.add(ruleService.findById(Integer.parseInt(statId)));
        }
        accident.setRules(rules);
        accident.setType(accidentTypeService.findById(typeId));
        accidentMem.add(accident);
    }

    /**
     * rIds - список статей
     * typeId - id типа происшествия
     */
    public void replace(Accident accident, String[] rIds, int typeId) {
        Set<Rule> rules = new HashSet<>();
        for (String statId : rIds) {
            rules.add(ruleService.findById(Integer.parseInt(statId)));
        }
        accident.setRules(rules);
        accident.setType(accidentTypeService.findById(typeId));
        accidentMem.replace(accident);
    }

    public Accident findById(int id) {
        return accidentMem.findById(id);
    }
}