package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * контроллер запросов
 */
@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient itemRequestClient;

    /**
     * создание запроса
     *
     * @param userId         id пользователя
     * @param itemRequestDto дто объект создания запроса
     * @return дто объект запроса
     */
    @PostMapping
    public ResponseEntity<Object> create(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("ItemRequest {} has been created", itemRequestDto);
        return itemRequestClient.create(userId, itemRequestDto);
    }

    /**
     * поиск всех запросов пользователя
     *
     * @param userId id  пользователя
     * @return список дто объектов всех запросов пользователя
     */
    @GetMapping
    public ResponseEntity<Object> findAllByUser(@Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting itemRequests for ownerId={} is successful", userId);
        return itemRequestClient.findAllByUser(userId);
    }

    /**
     * поиск всех запросов других пользователей
     *
     * @param userId id пользователя
     * @param from   начальный элемент выборки
     * @param size   размер выборки
     * @return список всех запросов других пользователей
     */
    @GetMapping("/all")
    public ResponseEntity<Object> findAllOnPage(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Getting itemRequests other users is successful");
        return itemRequestClient.findAllOnPage(userId, from, size);
    }

    /**
     * поиск по id
     *
     * @param requestId id запроса
     * @param userId    id  пользователя
     * @return дто объект запроса
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Positive @PathVariable("requestId") Long requestId) {
        log.info("Get requestId={} is successful", requestId);
        return itemRequestClient.getById(userId, requestId);
    }
}
