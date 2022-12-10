package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.model.Rule;
import ru.job4j.accidents.service.AccidentTypeService;

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
                     select a.*, t.*, ar.*, r.*
                     from accident a join type t on a.accident_type_id = t.type_id
                     join accident_rule ar on a.accident_id = ar.accident_id
                     join rule r on ar.rule_id = r.rule_id
                     """;
    private static final String ACCIDENT_BY_ID = "SELECT * FROM accident WHERE accident_id = ?";
    private static final String UPDATE_ACCIDENT =
            "UPDATE accident SET accident_name = ?, accident_text = ?, accident_address = ?, accident_type_id = ? WHERE accident_id = ?";
    private static final String DELETE_ACCIDENTS_RULES_BY_ACCIDENT
            = "DELETE FROM accident_rule WHERE accident_id = ?";

    public Accident save(Accident accident) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("accident")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", accident.getName());
        params.put("text", accident.getText());
        params.put("address", accident.getAddress());
        params.put("type_id", accident.getType().getId());
        int accId = (int) simpleJdbcInsert.executeAndReturnKey(params);
        accident.setId(accId);
        ruleJdbcTemplate.addAccidentId(accident.getRules(), accId);
        return accident;
    }

    public List<Accident> findAll() {
        return jdbc.query(ALL_ACCIDENTS, new AccidentExtractor());
    }

    public Accident findById(int id) {
        return jdbc.queryForObject(ACCIDENT_BY_ID,
                new BeanPropertyRowMapper<>(Accident.class), id);
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

    private class AccidentExtractor implements ResultSetExtractor<List<Accident>> {

        @Override
        public List<Accident> extractData(ResultSet rs) throws SQLException, DataAccessException {
           List<Accident> accidentList = new ArrayList<>();

           while (rs.next()) {
               Accident accident = new Accident();
               accident.setId(rs.getInt("accident_id"));
               accident.setName(rs.getString("accident_name"));
               accident.setText(rs.getString("accident_text"));
               accident.setAddress(rs.getString("accident_address"));

               AccidentType accidentType = new AccidentType();
               accidentType.setId(rs.getInt("type_id"));
               accidentType.setName(rs.getString("type_name"));
               accident.setType(accidentType);

               Rule rule = new Rule();
               rule.setId(rs.getInt("r.rule_id"));
               rule.setName(rs.getString("rule_name"));
               Set<Rule> rules = new HashSet<>();
               rules.add(rule);
               accident.setRules(rules);

               accidentList.add(accident);
           }
           return accidentList;
        }
    }
}