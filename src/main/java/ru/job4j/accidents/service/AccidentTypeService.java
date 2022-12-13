package ru.job4j.accidents.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.accidents.model.AccidentType;
import ru.job4j.accidents.repository.AccidentTypeJdbcTemplate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccidentTypeService {
    private final AccidentTypeJdbcTemplate accidentTypeJdbcTemplate;

    public List<AccidentType> findAll() {
        return accidentTypeJdbcTemplate.findAll();
    }

    public Optional<AccidentType> findById(int id) {
        return accidentTypeJdbcTemplate.findById(id);
    }
}