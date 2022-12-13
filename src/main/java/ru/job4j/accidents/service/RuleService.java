package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.repository.RuleJdbcTemplate;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RuleService {
    private final RuleJdbcTemplate ruleJdbcTemplate;

    public Collection<Rule> findAll() {
        return ruleJdbcTemplate.findAll();
    }

    public Optional<Rule> findById(int id) {
        return ruleJdbcTemplate.findById(id);
    }
}