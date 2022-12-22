package ru.job4j.accidents.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.Accident;

import java.util.List;
import java.util.Optional;

/**
 * Spring data jpa репозиторий для класса Accident
 */
public interface AccidentRepository extends CrudRepository<Accident, Integer> {
    @EntityGraph(attributePaths = {"type", "rules"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Accident> findAll();

    @EntityGraph(attributePaths = {"type", "rules"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Accident> findById(int id);
}


/* Альтернативный вариант
    @Query("select distinct a from Accident a join fetch a.rules")
    List<Accident> findAllAccidents();

    @Modifying
    @Query("update Accident as a set a.name = ?1, a.text = ?2, a.address = ?3 where a.id = ?4")
    void updateAccident(String name, String text, String address, int id);

 */