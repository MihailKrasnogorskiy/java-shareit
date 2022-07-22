package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * исключение выбрасываемое при попытке регистрации пользователя с уже зарегистрированным email
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNameIsBlankException extends RuntimeException {
    public UserNameIsBlankException() {
        super("User name can not is blank");
    }
}
