package by.fin.web.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * Represents an API error with a status code and a message.
 *
 * This class is used in the CustomExceptionHandler to represent an API error.
 * It contains a HttpStatus representing the HTTP status code that should be returned to the user,
 * and a message providing more details about the error.
 *
 * @see org.springframework.http.HttpStatus
 */
@Getter
@Setter
public class ApiError {

    private HttpStatus status;
    private String message;

    public ApiError(HttpStatus status) {
        this.status = status;
    }

}