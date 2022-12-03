package ru.job4j.accidents.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RuleMem {
    private final Map<Integer, Rule> ruleMap = new ConcurrentHashMap<>();

    public RuleMem() {
        ruleMap.put(1, new Rule(1, "Статья 1"));
        ruleMap.put(2, new Rule(2, "Статья 2"));
        ruleMap.put(3, new Rule(3, "Статья 3"));
    }

    public Collection<Rule> findAll() {
        return ruleMap.values();
    }

    public Rule finById(int id) {
        return ruleMap.get(id);
    }
}
