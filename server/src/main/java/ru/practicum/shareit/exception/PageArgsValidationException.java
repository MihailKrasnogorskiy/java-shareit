package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * исключение выбрасываемое при неправильных аргументах запрашиваемой страницы
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PageArgsValidationException extends RuntimeException {
    public PageArgsValidationException() {
        super("from must be positive and size must be more then 0");
    }
}
