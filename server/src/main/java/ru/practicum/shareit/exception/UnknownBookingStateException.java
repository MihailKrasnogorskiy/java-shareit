package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * исключение выбрасываемое при попытке запроса неизвестного варианта сортировки
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnknownBookingStateException extends RuntimeException {
    public UnknownBookingStateException() {
        super("Unknown state: UNSUPPORTED_STATUS");
    }
}
