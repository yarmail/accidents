package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.AccidentJdbcTemplate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AccidentService {
    private final AccidentJdbcTemplate accidentRepository;
    private final RuleService ruleService;
    private final AccidentTypeService accidentTypeService;

    /**
     * rIds - список статей
     * typeId - id типа происшествия
     */
    public boolean save(Accident accident, String[] rIds, int typeId) {
        AccidentType accidentType = accidentTypeService.findById(typeId);
        if (accidentType == null) {
            return false;
        }
        Set<Rule> rules = new HashSet<>();
        for (String statId : rIds) {
            Rule rule = ruleService.findById(Integer.parseInt(statId));
            if (rule == null) {
                return false;
            }
            rules.add(rule);
        }
        accident.setRules(rules);
        accident.setType(accidentType);
        accidentRepository.save(accident);
        return true;
    }

    public boolean replace(Accident accident, String[] rIds, int typeId) {
        AccidentType accidentType = accidentTypeService.findById(typeId);
        if (accidentType == null) {
            return false;
        }
        Set<Rule> rules = new HashSet<>();
        for (String statId : rIds) {
            Rule rule = ruleService.findById(Integer.parseInt(statId));
            if (rule == null) {
                return false;
            }
            rules.add(rule);
        }
        accident.setRules(rules);
        accident.setType(accidentType);
        accidentRepository.replace(accident);
        return true;
    }

    public Accident findById(int id) {
        return accidentRepository.findById(id);
    }

    public Collection<Accident> findAll() {
        return accidentRepository.findAll();
    }
}