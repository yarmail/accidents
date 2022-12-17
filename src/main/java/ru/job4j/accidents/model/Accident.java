package ru.job4j.accidents.model;

import lombok.*;
import javax.persistence.*;
import java.util.Set;


/**
 * правонарушение - происшествие
 * name - имя заявки? или имя правонарушителя?
 * text - описание правонарушения?
 * address - место нарушения?
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "accident")
public class Accident {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accident_id")
    private int id;

    @Column(name = "accident_name")
    private String name;

    @Column(name = "accident_text")
    private String text;

    @Column(name = "accident_address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "accident_type_id", referencedColumnName = "type_id")
    private AccidentType type;

    @ManyToMany
    @JoinTable(name = "accident_rule",
                joinColumns = @JoinColumn(name = "accident_id"),
                inverseJoinColumns = @JoinColumn(name = "rule_id"))
    private Set<Rule> rules;

    @Override
    public String toString() {
        return "Accident{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", text='" + text + '\''
                + ", address='" + address + '\''
                + ", type=" + type
                + ", rules=" + rules
                + '}';
    }
}



/* Примечания
Ещё такую конструкцию видел
@NamedEntityGraph(attributeNodes = {@NamedAttributeNode("rules"), @NamedAttributeNode("type")})
@Table(name = "accident")
public class Accident {

 */