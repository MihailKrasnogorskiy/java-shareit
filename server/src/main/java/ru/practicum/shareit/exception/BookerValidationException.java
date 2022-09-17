package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * исключение выбрасываемое при попытке поодтвердить или отклонить бронь не владельцем вещи
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookerValidationException extends RuntimeException {
    public BookerValidationException() {
        super("You are not booker");
    }
}
