package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * исключение выбрасываемое при попытке поодтвердить или отклонить бронь не владельцем вещи
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommentatorValidationException extends RuntimeException {
    public CommentatorValidationException() {
        super("You are not booker");
    }
}