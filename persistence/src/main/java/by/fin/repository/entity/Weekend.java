package by.fin.repository.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * The {@code Weekend} class represents a specific day that is either a weekend day or not.
 * It is annotated as a JPA entity and will be used to map onto a "weekends" table in the database.
 * It includes the {@code weekendId} as the primary key, the {@code calendarDate} which indicates the specific date,
 * and a boolean flag {@code isDayOff} which is true for weekends and holidays, false otherwise.
 * Lombok annotations are used for automatically generating getters, setters, toString, Builder,
 * no-argument constructor, and all-argument constructor methods.
 */
@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "weekends")
public class Weekend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekend_id")
    private Long weekendId;

    @Column(name = "calendar_date")
    private Date calendarDate;

    @Column(name = "is_day_off")
    private boolean isDayOff;
}
