package ru.job4j.accidents.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;

/**
 * Тип происшествия, например
 * Две машины
 * Машина и человек
 * Машина и велосипед
 */
@Setter
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "type")
public class AccidentType {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    @NonNull
    @ToString.Include
    private int id;

    @Column(name = "type_name")
    @NonNull
    @ToString.Include
    private String name;

    @OneToMany(mappedBy = "type")
    private List<Accident> accidents;
}