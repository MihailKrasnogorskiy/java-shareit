package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.BookingState;

/**
 * тестовый класс конвертора вариантов выборки
 */
public class BookingStateEnumConverterTest {
    private final BookingStateEnumConverter converter = new BookingStateEnumConverter();

    @Test
    void test36_convert() {
        assertEquals(BookingState.ALL, converter.convert("AlL"));
        assertEquals(BookingState.UNSUPPORTED_STATUS, converter.convert("IUHnjn"));
        assertEquals(BookingState.UNSUPPORTED_STATUS, converter.convert(null));
    }
}
