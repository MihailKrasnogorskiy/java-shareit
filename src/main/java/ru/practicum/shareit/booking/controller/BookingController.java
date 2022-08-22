package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreatingBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.exception.UnknownBookingStateException;

import javax.validation.Valid;
import java.util.List;

/**
 * класс контроллер бронирований
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    /**
     * создание бронирования
     *
     * @param userId     id пользователя
     * @param bookingDto объект для создания бронирования
     * @return dto бъект бронирования
     */

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                             @Valid @RequestBody CreatingBookingDto bookingDto) {
        return service.create(userId, bookingDto);
    }

    /**
     * измнение статуса бронирования
     *
     * @param userId    id пользователя
     * @param approved  статус бронирования
     * @param bookingId id бронирования
     * @return dto бъект бронирования
     */
    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam Boolean approved,
                              @PathVariable long bookingId) {
        return service.approve(userId, approved, bookingId);
    }

    /**
     * возвращение бронирования по id
     *
     * @param userId    id пользователя
     * @param bookingId id бронирования
     * @return dto бъект бронирования
     */
    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        return service.findById(userId, bookingId);
    }

    /**
     * возвращение всех бронирований пользователя
     *
     * @param userId id пользователя
     * @param state  вариант выборки (ALL, CURRENT, PAST, FUTURE, WAITING, UNSUPPORTED_STATUS, REJECTED)
     * @return список dto бъектов бронирования
     */
    @GetMapping
    public List<BookingDto> findAllByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam(defaultValue = "all") BookingState state) {
        if (state.equals(BookingState.UNSUPPORTED_STATUS)) {
            throw new UnknownBookingStateException();
        }
        return service.findAllByUser(userId, state);
    }

    /**
     * возвращение всех бронирований владельца
     *
     * @param ownerId id владельца вещи
     * @param state   вариант выборки (ALL, CURRENT, PAST, FUTURE, WAITING, UNSUPPORTED_STATUS, REJECTED)
     * @return список dto бъектов бронирования
     */
    @GetMapping("/owner")
    public List<BookingDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                           @RequestParam(defaultValue = "all") BookingState state) {
        if (state.equals(BookingState.UNSUPPORTED_STATUS)) {
            throw new UnknownBookingStateException();
        }
        return service.findAllByOwner(ownerId, state);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final UnknownBookingStateException e) {
        return new ErrorResponse(e.getMessage());
    }
}
