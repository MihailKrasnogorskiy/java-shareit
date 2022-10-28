package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * класс контроллер бронирований
 */
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    /**
     * возвращение всех бронирований пользователя
     *
     * @param userId     id пользователя
     * @param stateParam вариант выборки (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from       - начальный элемент
     * @param size       - размер выборки
     * @return список dto бъектов бронирования
     */
    @GetMapping
    public ResponseEntity<Object> findAllByUser(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.findAllByUser(userId, state, from, size);
    }

    /**
     * создание бронирования
     *
     * @param userId     id пользователя
     * @param bookingDto объект для создания бронирования
     * @return dto бъект бронирования
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody @Valid BookingDto bookingDto) {
        log.info("Creating booking {}, userId={}", bookingDto, userId);
        return bookingClient.create(userId, bookingDto);
    }

    /**
     * возвращение бронирования по id
     *
     * @param userId    id пользователя
     * @param bookingId id бронирования
     * @return dto бъект бронирования
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @Positive @PathVariable("bookingId") Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.findById(userId, bookingId);
    }

    /**
     * возвращение всех бронирований владельца
     *
     * @param ownerId    id владельца вещи
     * @param stateParam вариант выборки (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from       - начальный элемент
     * @param size       - размер выборки
     * @return список dto бъектов бронирования
     */
    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                 @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                 @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, ownerId={}, from={}, size={}", stateParam, ownerId, from, size);
        return bookingClient.findAllByOwner(ownerId, state, from, size);
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
    public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @RequestParam Boolean approved,
                                          @Positive @PathVariable("bookingId") long bookingId) {
        log.info("User with userId={} approve booking {}, approve={}", userId, bookingId, approved);
        return bookingClient.approve(userId, approved, bookingId);
    }
}
