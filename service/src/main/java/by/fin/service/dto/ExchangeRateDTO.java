package by.fin.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) class for Exchange Rate data.
 *
 * <p>This class is used for deserializing JSON data from the National Bank of the Republic of Belarus (NB RB) API.
 * It represents the exchange rate of a specific currency on a specific date. The class uses Jackson's
 * {@link com.fasterxml.jackson.annotation.JsonProperty} annotations to map JSON properties to Java fields.</p>
 *
 * <h2>Fields:</h2>
 * <ul>
 *     <li>curId: The currency identifier.</li>
 *     <li>date: The date of the exchange rate.</li>
 *     <li>curOfficialRate: The official exchange rate for the currency on the specified date.</li>
 * </ul>
 *
 * @see com.fasterxml.jackson.annotation.JsonProperty
 */
@Getter
@Setter
public class ExchangeRateDTO {

    @JsonProperty("Cur_ID")
    private int curId;

    @JsonProperty("Date")
    private LocalDateTime date;

    @JsonProperty("Cur_OfficialRate")
    private BigDecimal curOfficialRate;

}