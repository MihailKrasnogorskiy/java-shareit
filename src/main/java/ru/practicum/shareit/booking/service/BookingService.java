package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreatingBookingDTO;

public interface BookingService {
    BookingDto create(long userId, @Valid CreatingBookingDTO bookingDto);
}
