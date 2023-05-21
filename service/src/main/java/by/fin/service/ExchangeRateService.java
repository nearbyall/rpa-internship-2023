package by.fin.service;

import by.fin.repository.entity.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRateService {

    List<ExchangeRate> addExchangeRate(String currencyType, LocalDateTime startDate, LocalDateTime endDate);

    List<ExchangeRate> getAllExchangeRates(String currencyType);

    BigDecimal calculateAverageExchangeRate(String currencyType, int month, int year);

}