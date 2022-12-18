package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.AccidentHibernate;

import java.util.*;

@Service
@AllArgsConstructor
public class AccidentService {
    /*private final AccidentJdbcTemplate accidentRepository;*/
    private final RuleService ruleService;
    private final AccidentTypeService accidentTypeService;
    private final AccidentHibernate accidentHibernate;

    /**
     * rIds - список статей
     * typeId - id типа происшествия
     *
     * може быть save, add или create
     */
    public boolean save(Accident accident, String[] rIds, int typeId) {
        Optional<AccidentType> accidentType = accidentTypeService.findById(typeId);
        if (accidentType.isEmpty()) {
            return false;
        }
        Set<Rule> rules = new HashSet<>();
        for (String statId : rIds) {
            Optional<Rule> rule = ruleService.findById(Integer.parseInt(statId));
            if (rule.isEmpty()) {
                return false;
            }
            rules.add(rule.get());
        }
        accident.setRules(rules);
        accident.setType(accidentType.get());
        accidentHibernate.save(accident);
        return true;
    }

    public boolean replace(Accident accident, String[] rIds, int typeId) {
        Optional<AccidentType> accidentType = accidentTypeService.findById(typeId);
        if (accidentType.isEmpty()) {
            return false;
        }
        Set<Rule> rules = new HashSet<>();
        for (String statId : rIds) {
            Optional<Rule> rule = ruleService.findById(Integer.parseInt(statId));
            if (rule.isEmpty()) {
                return false;
            }
            rules.add(rule.get());
        }
        accident.setRules(rules);
        accident.setType(accidentType.get());
        accidentHibernate.replace(accident);
        return true;
    }

    public Optional<Accident> findById(int id) {
        return accidentHibernate.findById(id);
    }

    public List<Accident> findAll() {
        return accidentHibernate.findAll();
    }
}