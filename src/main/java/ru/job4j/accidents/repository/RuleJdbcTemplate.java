package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Rule;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@AllArgsConstructor
public class RuleJdbcTemplate {
    private final JdbcTemplate jdbc;
    private final DataSource dataSource;

    private static final String RULES_BY_ACCIDENT =
            "SELECT r.rule_id, r.rule_name FROM accident_rule a "
                    + "JOIN rule r ON r.rule_id = a.rule_id WHERE accident_id = ?";
    private static final String RULE_BY_ID = "select * from rule where rule_id = ?";

    public List<Rule> findAll() {
        return jdbc.query("SELECT * FROM rule",
                new RowMapper<Rule>() {
                    @Override
                    public Rule mapRow(ResultSet rs, int row) throws SQLException {
                        Rule rule = new Rule();
                        rule.setId(rs.getInt("rule_id"));
                        rule.setName(rs.getString("rule_name"));
                        return rule;
                    }
                });
    }

    public Optional<Rule> findById(int id) {
        Rule rule1 = jdbc.queryForObject(
                "SELECT * FROM rule WHERE rule_id = ?",
                new RowMapper<Rule>() {
                    @Override
                    public Rule mapRow(ResultSet rs, int row) throws SQLException {
                        Rule rule = new Rule();
                        rule.setId(id);
                        rule.setName(rs.getString("rule_name"));
                        return rule;
                    }
                }, id);
        return Optional.ofNullable(rule1);
    }

    public void addAccidentId(Set<Rule> rules, int accId) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("accident_rule")
                .usingGeneratedKeyColumns("accident_rule_id");
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