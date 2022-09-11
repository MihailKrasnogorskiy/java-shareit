package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * тестовый класс класса ошибки
 */
class ErrorResponseTest {

    @Test
    void test37_getError() {
        final String error = "error";
        ErrorResponse response = new ErrorResponse(error);
        assertEquals(error, response.getError());
    }

}