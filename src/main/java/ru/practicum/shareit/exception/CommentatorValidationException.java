package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * исключение выбрасываемое при попытке оставить комментарий пользователем не бравщим вещь в аренду
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommentatorValidationException extends RuntimeException {
    public CommentatorValidationException() {
        super("You are not booker");
    }
}
