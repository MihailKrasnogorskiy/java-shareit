package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * исключение выбрасываемое при попытке создания бронирования с датой окончания раньше даты начала
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EndDateValidateException extends RuntimeException {
    public EndDateValidateException() {
        super("EndDate is before StartDate");
    }
}