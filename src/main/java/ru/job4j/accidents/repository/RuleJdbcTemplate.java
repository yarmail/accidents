package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;
import javax.sql.DataSource;
import java.util.*;

@Repository
@AllArgsConstructor
public class RuleJdbcTemplate {
    private final JdbcTemplate jdbc;
    private final DataSource dataSource;

    private static final String RULES_BY_ACCIDENT =
            "select r.rule_id, r.rule_name from accident_rule a "
                    + "join rule r on r.rule_id = a.rule_id where accident_id = ?";
    private static final String RULE_BY_ID = "select * from rule where rule_id = ?";
    private static final String ALL_RULES = "select * from rule";

    public List<Rule> findAll() {
        return jdbc.query(ALL_RULES, new BeanPropertyRowMapper<>(Rule.class));
    }

    public Rule findById(int id) {
        return jdbc.queryForObject(RULE_BY_ID,
                new BeanPropertyRowMapper<>(Rule.class), id);
    }

    public void addAccidentId(Set<Rule> rules, int accId) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("accident_rule")
                .usingGeneratedKeyColumns("id");
        rules.forEach(rule -> {
            Map<String, Object> params = new HashMap<>();
            params.put("accident_id", accId);
            params.put("rule_id", rule.getId());
            jdbcInsert.execute(params);
        });
    }

    public Set<Rule> findRulesByAccident(int accId) {
        return new HashSet<>(jdbc.query(RULES_BY_ACCIDENT,
                    new BeanPropertyRowMapper<>(Rule.class), accId));
        }
}