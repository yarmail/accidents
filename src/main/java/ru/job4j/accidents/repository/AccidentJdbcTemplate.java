package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate {
    private final JdbcTemplate jdbc;
    private final DataSource dataSource;
    private final RuleJdbcTemplate ruleJdbcTemplate;

    private static final String ALL_ACCIDENTS = """
        SELECT *
        FROM accident a JOIN type t ON a.accident_type_id = t.type_id
        JOIN accident_rule ar ON a.accident_id = ar.accident_id
        JOIN rule r ON ar.rule_id = r.rule_id
    """;
    private static final String ACCIDENT_BY_ID = """
        SELECT *
        FROM accident a JOIN type t ON a.accident_type_id = t.type_id
        JOIN accident_rule ar ON a.accident_id = ar.accident_id
        JOIN rule r ON ar.rule_id = r.rule_id
        WHERE a.accident_id = ?
    """;
    private static final String UPDATE_ACCIDENT =
            "UPDATE accident SET accident_name = ?, accident_text = ?, accident_address = ?, "
                    + "accident_type_id = ? WHERE accident_id = ?";
    private static final String DELETE_ACCIDENTS_RULES_BY_ACCIDENT =
            "DELETE FROM accident_rule WHERE accident_id = ?";

    public Accident save(Accident accident) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("accident")
                .usingGeneratedKeyColumns("accident_id");
        Map<String, Object> params = new HashMap<>();
        params.put("accident_name", accident.getName());
        params.put("accident_text", accident.getText());
        params.put("accident_address", accident.getAddress());
        params.put("accident_type_id", accident.getType().getId());
        int accId = (int) simpleJdbcInsert.executeAndReturnKey(params);
        accident.setId(accId);
        ruleJdbcTemplate.addAccidentId(accident.getRules(), accId);
        return accident;
    }

    public List<Accident> findAll() {
        Collection<Accident> accidents =
                jdbc.query(ALL_ACCIDENTS, new AccidentExtractor()).values();
        return accidents.size() > 0 ? new ArrayList<>(accidents) : new ArrayList<>();
    }

    public Optional<Accident> findById(int id) {
        return jdbc.query(ACCIDENT_BY_ID, new AccidentExtractor(), id)
                        .values().stream().findFirst();
        }

    public void replace(Accident accident) {
        jdbc.update(UPDATE_ACCIDENT,
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getType().getId(),
                accident.getId());
        jdbc.update(DELETE_ACCIDENTS_RULES_BY_ACCIDENT, accident.getId());
        ruleJdbcTemplate.addAccidentId(accident.getRules(), accident.getId());
    }

    private class AccidentExtractor implements ResultSetExtractor<Map<Integer, Accident>> {
        Map<Integer, Accident> result = new HashMap<>();

        @Override
        public Map<Integer, Accident> extractData(ResultSet rs) throws SQLException, DataAccessException {
            while (rs.next()) {
                int accidentId = rs.getInt("accident_id");
                Accident accident = new Accident();
                accident.setId(accidentId);
                accident.setName(rs.getString("accident_name"));
                accident.setText(rs.getString("accident_text"));
                accident.setAddress(rs.getString("accident_address"));

                AccidentType accidentType = new AccidentType();
                accidentType.setId(rs.getInt("type_id"));
                accidentType.setName(rs.getString("type_name"));
                accident.setType(accidentType);

                Set<Rule> rules = new HashSet<>();
                accident.setRules(rules);

                result.putIfAbsent(accidentId, accident);

                Rule rule = new Rule();
                rule.setId(rs.getInt("rule_id"));
                rule.setName(rs.getString("rule_name"));

                result.get(accidentId).getRules().add(rule);
            }
            return result;
        }
    }
}
/* Альтернативные варианты от Станислава

    private final ResultSetExtractor<Map<Integer, Accident>> extractor = (rs) -> {
        Map<Integer, Accident> result = new HashMap<>();
        while (rs.next()) {
            Accident accident = Accident.of(
                    rs.getString("name"),
                    rs.getString("text"),
                    rs.getString("address"),
                    AccidentType.of(
                            rs.getInt("type_id"),
                            rs.getString("t_name")
                    ),
                    new HashSet<>()
            );
            accident.setId(rs.getInt("id"));
            result.putIfAbsent(accident.getId(), accident);
            result.get(accident.getId()).addRule(
                    Rule.of(
                            rs.getInt("r_id"),
                            rs.getString("r_name")
                    )
            );
        }
        return result;
    };
 */
/*
    private final RowMapper<Accident> accidentRowMapper = ((rs, i) -> {
        Accident accident = Accident.of(
                rs.getString("name"),
                rs.getString("text"),
                rs.getString("address"),
                AccidentType.of(
                        rs.getInt("type_id"),
                        rs.getString("t_name")
                ),
                new HashSet<>()
        );
        accident.setId(rs.getInt("id"));
        return accident;
    });
 */