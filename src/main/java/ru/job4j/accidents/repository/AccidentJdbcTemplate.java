package ru.job4j.accidents.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.job4j.accidents.model.Accident;
import ru.job4j.accidents.service.AccidentTypeService;

import java.util.List;

@Repository
@AllArgsConstructor
public class AccidentJdbcTemplate {
    private final JdbcTemplate jdbc;
    private final AccidentTypeService typeService;

    public void save(Accident accident) {
        jdbc.update("insert into accident (name, text, address, type_id) "
                         + "values(?, ?, ?, ?)",
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getType().getId()
        );
    }

    public void replace(Accident accident) {
        jdbc.update("update accident set name = ?, text = ?, address = ?,"
                        + "type_id = ? where id = ?",
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getType().getId(),
                accident.getId()
        );
    }

    public List<Accident> findAll() {
        return jdbc.query("select * from accident",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(rs.getInt("id"));
                    accident.setName(rs.getString("name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    accident.setType(typeService.findById(rs.getInt("type_id")));
                    return accident;
                });
    }

    public Accident findById(int id) {
        return jdbc.queryForObject("select name, text, address, type_id from accident where id = ?",
                (rs, row) -> {
                    Accident accident = new Accident();
                    accident.setId(id);
                    accident.setName(rs.getString("name"));
                    accident.setText(rs.getString("text"));
                    accident.setAddress(rs.getString("address"));
                    accident.setType(typeService.findById(rs.getInt("type_id")));
                    return accident;
                }, id);
    }
}