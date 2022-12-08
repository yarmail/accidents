package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.service.AccidentTypeService;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate {
    private final JdbcTemplate jdbc;
    private final DataSource dataSource;
    private final AccidentTypeService typeService;
    private final RuleJdbcTemplate ruleJdbcTemplate;

    private static final String ALL_ACCIDENTS =
            "select distinct a.id, a.name, a.text, a.address, a.type_id, a_t.name "
                    + "from accident a join acc_type a_t ON a.type_id = a_t.id";
    private static final String ACCIDENT_BY_ID = "SELECT * FROM accident WHERE id = ?";
    private static final String UPDATE_ACCIDENT =
            "UPDATE accident SET name = ?, text = ?, address = ?, type_id = ? WHERE id = ?";
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
        return jdbc.query(ALL_ACCIDENTS, new AccidentMapper());
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

    private class AccidentMapper implements RowMapper<Accident> {
        private int id = -1;

        public AccidentMapper() {
        }

        public AccidentMapper(int id) {
            this.id = id;
        }

        /**
         * Заполняем Accident из таблицы
         * Если я правильно понимаю такой запрос
         * нужно делать к объедененной таблице
         */
        @Override
        public Accident mapRow(ResultSet rs, int rowNum) throws SQLException {
            Accident accident = new Accident();
            if (id == -1) {
                accident.setId(rs.getInt("id"));
            } else {
                accident.setId(id);
            }
            accident.setName(rs.getString("name"));
            accident.setText(rs.getString("text"));
            accident.setAddress(rs.getString("address"));

            AccidentType accidentType = new AccidentType();
            accidentType.setId(rs.getInt("type_id"));
            accidentType.setName(rs.getString("name"));
            accident.setType(accidentType);
            accident.setRules(ruleJdbcTemplate.findRulesByAccident(accident.getId()));
            return accident;
        }
    }
}