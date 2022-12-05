package ru.job4j.accidents.service;

import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
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

    /**
     * проверка на плохие значения
     * rIds - список статей
     * typeId - id типа происшествия
     * operation - тип дальнейшей операции
     */
    public boolean checkValue(Accident accident, String[] rIds,
                              int typeId, String operation) {
        boolean result = false;
        AccidentType accidentType = accidentTypeService.findById(typeId);
        if ((accidentType != null) || (rIds != null)) {
            completeAccident(accident, rIds, accidentType, operation);
            result = true;
        }
        return result;
    }

    /**
     * Комплектуем Accident дополнениями и сохраняем
     * rIds - статьи, accidentType - тип происшествия
     *
     */
    private void completeAccident(Accident accident, String[] rIds,
                                  AccidentType accidentType, String operation) {
        Set<Rule> rules = new HashSet<>();
        for (String statId : rIds) {
            rules.add(ruleService.findById(Integer.parseInt(statId)));
        }
        accident.setRules(rules);
        accident.setType(accidentType);
        if (operation.equals("add")) {
            add(accident);
        }
        if (operation.equals("replace")) {
            replace(accident);
        }
    }

    public void add(Accident accident) {
        accidentMem.add(accident);
    }

    public Collection<Accident> findAll() {
        return accidentMem.findAll();
    }

    public void replace(Accident accident) {
        accidentMem.replace(accident);
    }

    public Accident findById(int id) {
        return accidentMem.findById(id);
    }
}