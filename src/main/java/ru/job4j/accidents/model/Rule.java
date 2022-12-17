package ru.job4j.accidents.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * В этом уроке мы добавим список статей, связанных с инцидентом.
 * Добавим модель, описывающую статьи нарушений.
 */
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "rule")
public class Rule {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    @NonNull
    @ToString.Include
    private int id;

    @Column(name = "rule_name")
    @NonNull
    @ToString.Include
    private String name;

    @ManyToMany(mappedBy = "rules")
    private Set<Accident> accidents;
}