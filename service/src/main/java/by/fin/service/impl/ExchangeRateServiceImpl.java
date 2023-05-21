package by.fin.service.impl;

import by.fin.repository.ExchangeRateRepository;
import by.fin.repository.WeekendRepository;
import by.fin.repository.entity.ExchangeRate;
import by.fin.repository.entity.Weekend;
import by.fin.service.ExchangeRateService;
import by.fin.service.dto.CurrencyDTO;
import by.fin.service.dto.ExchangeRateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to manage exchange rates in the application.
 *
 * The ExchangeRateService class provides methods to add, fetch, and calculate average
 * exchange rates for a specified currency type and for a range of dates.
 * The class makes use of the NB RB API to fetch exchange rate data, and the ExchangeRateRepository
 * to add and fetch data from the database.
 *
 * The main methods of the class are:
 *  - addExchangeRate: adds exchange rates for a specified currency type and for a date range into the database.
 *  - getAllExchangeRates: fetches all exchange rates for a specified currency type from the database.
 *  - calculateAverageRateForMonth: calculates the average exchange rate for a specified currency type over a given month and year.
 *
 * The service class also contains auxiliary private methods to assist the main methods.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final WeekendRepository weekendRepository;
    private final RestTemplate restTemplate;

    /**
     * Adds exchange rates for a specified currency type and for a date range into the database.
     *
     * The method makes a call to the NB RB API to get exchange rate data for a specified currency type
     * and for a range of dates provided by the start and end dates.
     * Before making the API call, it checks if the start date is before the end date, if the provided currency type
     * is in the list of correct types ("USD", "EUR", "RUB", "JPY"), and if exchange rates for the specified
     * period already exist in the database.
     * If all checks pass, it makes the API call, transforms the response into a list of ExchangeRate objects
     * and saves them into the database.
     *
     * @param currencyType the type of the currency for which exchange rates are to be added.
     *                     Example: "USD", "EUR", "GBP".
     * @param startDate the start date of the period for which exchange rates are to be added.
     * @param endDate the end date of the period for which exchange rates are to be added.
     * @return a List of ExchangeRate objects that were added into the database.
     * @throws IllegalArgumentException if start date is after end date, or if an incorrect currency type is provided.
     * @throws IllegalStateException if exchange rates for the specified period already exist in the database.
     * @throws RuntimeException if there is a failure to get data from the NB RB API.
     */
    @Override
    public List<ExchangeRate> addExchangeRate(String currencyType, LocalDateTime startDate, LocalDateTime endDate) {
        // Optional: check date
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        // Optional: check if daily rates already exist for the selected period
        List<ExchangeRate> existingRates = exchangeRateRepository.findAllByCurrencyTypeAndDateBetween(currencyType, startDate, endDate);
        if (!existingRates.isEmpty()) {
            throw new IllegalStateException("Exchange rates already exist for the selected period");
        }
        // Call NB RB API
        String url = "https://api.nbrb.by/exrates/rates/dynamics/" + getCurrencyId(currencyType) +
                "?startdate=" + startDate +
                "&enddate=" + endDate;
        ExchangeRateDTO[] rates = restTemplate.getForObject(url, ExchangeRateDTO[].class);
        if (rates == null) {
            throw new RuntimeException("Failed to get data from NB RB API");
        }
        // Transform ExchangeRateDTO[] into List<ExchangeRate> and save them in the DB
        List<ExchangeRate> exchangeRates = Arrays.stream(rates)
                .map(rs -> new ExchangeRate(null, currencyType, rs.getDate(), rs.getCurOfficialRate()))
                .collect(Collectors.toList());
        return exchangeRateRepository.saveAll(exchangeRates);
    }

    /**
     * Fetches all exchange rates for a specified currency type from the database.
     *
     * The method fetches and returns a list of all ExchangeRate objects of the specified currency type stored in the database.
     * Before fetching, it checks if the provided currency type is in the list of correct types ("USD", "EUR", "RUB", "JPY").
     *
     * @param currencyType the type of the currency for which exchange rates are to be fetched.
     *                     Example: "USD", "EUR", "GBP".
     * @return a List of ExchangeRate objects of the specified currency type.
     * @throws IllegalArgumentException if an incorrect currency type is provided.
     */
    public List<ExchangeRate> getAllExchangeRates(String currencyType) {
        // Optional: check for correct currency type
        if (!exchangeRateRepository.existsByCurrencyType(currencyType)) {
            throw new IllegalArgumentException("Currency type not found: " + currencyType);
        }

        return exchangeRateRepository.findByCurrencyType(currencyType);
    }

    /**
     * Calculates the average exchange rate for a specified currency type over a given month and year.
     *
     * The method fetches exchange rate data from the database for a given currency type and for all days within
     * the specified month and year. Weekends are excluded from the calculation as specified by the WeekendRepository.
     *
     * @param currencyType the type of the currency for which the average exchange rate is to be calculated.
     *                     Example: "USD", "EUR", "GBP".
     * @param month the month for which the average exchange rate is to be calculated. It is a integer where
     *              1 represents January and 12 represents December.
     * @param year the year for which the average exchange rate is to be calculated.
     * @return the average exchange rate as a BigDecimal. The average is calculated as the arithmetic mean
     *         of all daily exchange rates for non-weekend days.
     * @throws RuntimeException if no exchange rates are found for the specified period.
     * @throws IllegalArgumentException if an incorrect currency type is provided.
     */
    public BigDecimal calculateAverageExchangeRate(String currencyType, int month, int year) {
        // Optional: check for correct currency type
        if (!exchangeRateRepository.existsByCurrencyType(currencyType)) {
            throw new IllegalArgumentException("Currency type not found: " + currencyType);
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        // Get all exchange rates for the specified period
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAllByCurrencyTypeAndDateBetween(currencyType, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));

        ZoneId zoneId = ZoneId.systemDefault();
        Date start = Date.from(startDate.atStartOfDay().atZone(zoneId).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(zoneId).toInstant());

        // Get all weekends for the specified period
        List<Weekend> weekends = weekendRepository.findAllByDateBetween(start, end);

        BigDecimal product = BigDecimal.ONE;
        int count = 0;
        for (ExchangeRate exchangeRate : exchangeRates) {
            Date date = Date.from(exchangeRate.getDate().atZone(zoneId).toInstant());

            if (weekends.stream().noneMatch(weekend -> weekend.getCalendarDate().equals(date) && weekend.isDayOff())) {
                product = product.multiply(exchangeRate.getRate());
                count++;
            }
        }

        if (count > 0) {
            BigDecimal geometricMean = BigDecimal.valueOf(Math.pow(product.doubleValue(), 1.0/count));
            return geometricMean.setScale(2, RoundingMode.HALF_UP);
        } else {
            throw new RuntimeException("No exchange rates found for the specified period");
        }
    }

    /**
     * Fetches the currency ID for a specified currency type from the NB RB API.
     *
     * The method makes a call to the NB RB API to get the currency ID of the specified currency type.
     * The ID is needed for making subsequent API calls.
     *
     * @param currencyType the type of the currency for which the currency ID is to be fetched.
     *                     Example: "USD", "EUR", "GBP".
     * @return the currency ID as a String.
     * @throws RuntimeException if there is a failure to get data from the NB RB API.
     * @throws IllegalArgumentException if an incorrect currency type is provided.
     */
    private String getCurrencyId(String currencyType) {
        try {
            String url = "https://api.nbrb.by/exrates/rates/" + currencyType + "?parammode=2";
            CurrencyDTO currencyData = restTemplate.getForObject(url, CurrencyDTO.class);
            return String.valueOf(currencyData.getCurId());
        } catch (HttpClientErrorException.NotFound ex) {
            throw new IllegalArgumentException("Currency type not found: " + currencyType, ex);
        } catch (RestClientException ex) {
            throw new RuntimeException("Failed to get data from NB RB API", ex);
        }
    }

}