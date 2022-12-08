package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;
import java.util.List;

@Repository
@AllArgsConstructor
public class AccidentTypeJdbcTemplate {
    private final JdbcTemplate jdbc;

    public List<AccidentType> findAll() {
        return jdbc.query("select * from acc_type",
                new BeanPropertyRowMapper<>(AccidentType.class));
    }

    public AccidentType findById(int id) {
        return jdbc.queryForObject("select * from acc_type where id = ?",
                new BeanPropertyRowMapper<>(AccidentType.class), id);
    }
}
