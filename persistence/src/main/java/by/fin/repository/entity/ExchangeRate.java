package by.fin.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The {@code ExchangeRate} class represents the exchange rate of a specific currency on a particular date.
 * It is annotated as a JPA entity and will be used to map onto a table in the database.
 * It includes the {@code id} as the primary key, the {@code currencyType} which specifies the type of currency,
 * the {@code date} on which the exchange rate is effective, and the {@code rate} which is the actual exchange rate.
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
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String currencyType;

    private LocalDateTime date;

    private BigDecimal rate;
}