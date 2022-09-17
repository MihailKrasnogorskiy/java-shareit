package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

/**
 * клиент для отправки запросов в BookingController shareIt-server
 */
@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /**
     * возвращение всех бронирований пользователя
     *
     * @param userId id пользователя
     * @param state  вариант выборки (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from   - начальный элемент
     * @param size   - размер выборки
     * @return список dto бъектов бронирования
     */
    public ResponseEntity<Object> findAllByUser(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    /**
     * создание бронирования
     *
     * @param userId     id пользователя
     * @param bookingDto объект для создания бронирования
     * @return dto бъект бронирования
     */
    public ResponseEntity<Object> create(long userId, BookingDto bookingDto) {
        return post("", userId, bookingDto);
    }

    /**
     * возвращение бронирования по id
     *
     * @param userId    id пользователя
     * @param bookingId id бронирования
     * @return dto бъект бронирования
     */
    public ResponseEntity<Object> findById(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    /**
     * возвращение всех бронирований владельца
     *
     * @param ownerId id владельца вещи
     * @param state   вариант выборки (ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED)
     * @param from    - начальный элемент
     * @param size    - размер выборки
     * @return список dto бъектов бронирования
     */
    public ResponseEntity<Object> findAllByOwner(long ownerId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", ownerId, parameters);
    }

    /**
     * измнение статуса бронирования
     *
     * @param userId    id пользователя
     * @param approved  статус бронирования
     * @param bookingId id бронирования
     * @return dto бъект бронирования
     */
    public ResponseEntity<Object> approve(long userId, Boolean approved, long bookingId) {
        return patch("/" + bookingId + "?approved=" + approved, userId);
    }
}
