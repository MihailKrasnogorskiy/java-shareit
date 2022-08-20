package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreatingBookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import javax.validation.Valid;
import java.util.List;

// интерфейс сервиса бронирований
public interface BookingService {
    /**
     * создание бронирования
     *
     * @param userId     id пользователя
     * @param bookingDto объект для создания бронирования
     * @return dto бъект бронирования
     */
    BookingDto create(long userId, @Valid CreatingBookingDto bookingDto);

    /**
     * измнение статуса бронирования
     *
     * @param userId    id пользователя
     * @param approved  статус бронирования
     * @param bookingId id бронирования
     * @return dto бъект бронирования
     */
    BookingDto approve(long userId, Boolean approved, long bookingId);

    /**
     * возвращение бронирования по id
     *
     * @param userId    id пользователя
     * @param bookingId id бронирования
     * @return dto бъект бронирования
     */
    BookingDto findById(long userId, long bookingId);

    /**
     * возвращение всех бронирований пользователя
     *
     * @param userId id пользователя
     * @param state  вариант выборки
     * @return список dto бъектов бронирования
     */
    List<BookingDto> findAllByUser(long userId, BookingState state);

    /**
     * возвращение всех бронирований владельца
     *
     * @param ownerId id владельца вещи
     * @param state   вариант выборки
     * @return список dto бъектов бронирования
     */
    List<BookingDto> findAllByOwner(long ownerId, BookingState state);

    /**
     * возвращение всех бронирований вещи
     *
     * @param itemId id вещи
     * @return список бронирований
     */
    List<BookingDto> findAllByItemId(long itemId);
}
