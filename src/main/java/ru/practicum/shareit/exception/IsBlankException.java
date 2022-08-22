package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * исключение выбрасываемое при попытке создания пустого значения чего-либо
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IsBlankException extends RuntimeException {
    public IsBlankException(String massage) {
        super(massage + " can not is blank");
    }
}
