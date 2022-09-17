package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * исключение выбрасываемое при попытке зарезервировать недоступную для резерва вещь
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ItemUnavailableException extends RuntimeException {
    public ItemUnavailableException() {
        super("This item is not available");
    }
}
