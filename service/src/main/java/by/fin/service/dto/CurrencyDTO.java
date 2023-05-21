package by.fin.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyDTO {

    @JsonProperty("Cur_ID")
    private int curId;

}