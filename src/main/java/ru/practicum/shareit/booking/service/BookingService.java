package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreatingBookingDTO;
import ru.practicum.shareit.booking.model.BookingState;

import javax.validation.Valid;
import java.util.List;

public interface BookingService {
    BookingDto create(long userId, @Valid CreatingBookingDTO bookingDto);

    BookingDto approve(long userId, Boolean approved, long bookingId);

    BookingDto findById(long userId, long bookingId);

    List<BookingDto> findAllByUser(long userId, BookingState state);

    List<BookingDto> findAllByOwner(long ownerId, BookingState state);

    List<BookingDto> findAllByItemId(long itemId);
}
