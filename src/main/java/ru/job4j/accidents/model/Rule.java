package ru.job4j.accidents.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * В этом уроке мы добавим список статей, связанных с инцидентом.
 * Добавим модель, описывающую статьи нарушений.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rule {
    @EqualsAndHashCode.Include
    private int id;
    private String name;
}
