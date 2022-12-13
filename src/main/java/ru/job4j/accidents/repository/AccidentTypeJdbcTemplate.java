package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.AccidentType;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccidentTypeJdbcTemplate {
    private final JdbcTemplate jdbc;

    public List<AccidentType> findAll() {
        return jdbc.query("SELECT * from type",
                (rs, row) -> {
                    AccidentType type = new AccidentType();
                    type.setId(rs.getInt("type_id"));
                    type.setName(rs.getString("type_name"));
                    return type;
                });
    }

    public Optional<AccidentType> findById(int id) {
        AccidentType accidentType = jdbc.queryForObject(
                "SELECT type_name FROM type WHERE type_id = ?",
                (rs, row) -> {
                    AccidentType type = new AccidentType();
                    type.setId(id);
                    type.setName(rs.getString("type_name"));
                    return type;
                }, id);
        return Optional.ofNullable(accidentType);
    }
}