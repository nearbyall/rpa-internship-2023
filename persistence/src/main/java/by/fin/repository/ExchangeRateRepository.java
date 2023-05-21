package by.fin.repository;

import by.fin.repository.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ExchangeRate repository interface for performing CRUD operations on the ExchangeRate entity.
 *
 * <p>This interface extends Spring's JpaRepository interface and utilizes its methods for performing
 * CRUD operations. It also declares several custom methods for finding ExchangeRate objects
 * based on certain conditions, such as the currency type and date range.</p>
 *
 * <h2>Declared methods:</h2>
 * <ul>
 *     <li>findByCurrencyType: Finds all exchange rates for a particular currency type.</li>
 *     <li>findAllByCurrencyTypeAndDateBetween: Finds all exchange rates for a particular currency type within a certain date range.</li>
 *     <li>existsByCurrencyType: Checks whether there are any exchange rates for a particular currency type.</li>
 * </ul>
 *
 * @see by.fin.repository.entity.ExchangeRate
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    List<ExchangeRate> findByCurrencyType(String currencyType);

    List<ExchangeRate> findAllByCurrencyTypeAndDateBetween(String currencyType, LocalDateTime startDate, LocalDateTime endDate);

    boolean existsByCurrencyType(String currencyType);

}
