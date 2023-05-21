package by.fin.web.controller;

import by.fin.repository.entity.ExchangeRate;
import by.fin.service.ExchangeRateService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Rest controller for managing exchange rates.
 *
 * <p>This controller provides the entry points for the API operations related to the exchange rates. The API operations
 * include fetching all exchange rates for a specific currency type, adding new exchange rates for a specific period and
 * currency type, and calculating average exchange rates for a specific month and year.</p>
 *
 * <p>It interacts with the {@link ExchangeRateService} to perform the business logic operations.</p>
 *
 * <h2>API Endpoints:</h2>
 * <ul>
 *     <li>POST /api/exchangeRates: Adds new exchange rates for a specific period and currency type.</li>
 *     <li>GET /api/exchangeRates: Fetches all exchange rates for a specific currency type from DB.</li>
 *     <li>GET /api/exchangeRates/average: Calculates the average exchange rate for a specific currency type for a specific month and year.</li>
 * </ul>
 *
 * @see by.fin.service.ExchangeRateService
 */
@RestController
@RequestMapping("/api/exchangeRates")
public class ExchangeRateController {

    private final ExchangeRateService service;

    /**
     * Instantiates a new Exchange rate controller.
     *
     * @param service the exchange rate service
     */
    public ExchangeRateController(ExchangeRateService service) {
        this.service = service;
    }

    /**
     * Adds exchange rate and returns the list of added rates.
     *
     * @param currencyType the currency type
     * @param startDate    the start date
     * @param endDate      the end date
     * @return the list of added exchange rates
     */
    @PostMapping
    public ResponseEntity<List<ExchangeRate>> addExchangeRate(
            @RequestParam String currencyType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ExchangeRate> exchangeRates = service.addExchangeRate(currencyType, startDate, endDate);
        return ResponseEntity.ok(exchangeRates);
    }

    /**
     * Gets all exchange rates for a specific currency type.
     *
     * @param currencyType the currency type
     * @return the list of exchange rates
     */
    @GetMapping
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates(@RequestParam String currencyType) {
        List<ExchangeRate> exchangeRates = service.getAllExchangeRates(currencyType);
        return ResponseEntity.ok(exchangeRates);
    }

    /**
     * Calculates average exchange rate for a specific currency type within a certain month and year.
     *
     * @param currencyType the currency type
     * @param month        the month
     * @param year         the year
     * @return the average exchange rate
     */
    @GetMapping("/average")
    public ResponseEntity<BigDecimal> calculateAverageExchangeRate(
            @RequestParam String currencyType, @RequestParam int month, @RequestParam int year) {
        BigDecimal averageRate = service.calculateAverageExchangeRate(currencyType, month, year);
        return ResponseEntity.ok(averageRate);
    }

}