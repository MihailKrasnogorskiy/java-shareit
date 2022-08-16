package ru.practicum.shareit.booking.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Locale;

@Component
public class BookingStateEnumConverter implements Converter<String, BookingState> {
    @Override
    public BookingState convert(String source) {
        try {
            return source.isEmpty() ? null : BookingState.valueOf(source.trim().toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return BookingState.UNSUPPORTED_STATUS;
        }
    }
}
