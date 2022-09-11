package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.CreatedItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * // контроллер запросов
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @Autowired
    public ItemRequestController(ItemRequestService service) {
        this.service = service;
    }

    /**
     * создание запроса
     *
     * @param userId id пользователя
     * @param dto    дто объект создания запроса
     * @return дто объект запроса
     */
    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @Valid @RequestBody CreatedItemRequestDto dto) {
        return service.create(userId, dto);
    }

    /**
     * поиск по id
     *
     * @param requestId id запроса
     * @param userId    id  пользователя
     * @return дто объект запроса
     */
    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        return service.getById(requestId, userId);
    }

    /**
     * поиск всех запросов пользователя
     *
     * @param userId id  пользователя
     * @return список дто объектов всех запросов пользователя
     */
    @GetMapping
    public List<ItemRequestDto> findAllByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.findAllByUser(userId);
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
    public List<ItemRequestDto> findAllOnPage(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "20") Integer size) {
        return service.findAllOnPage(userId, from, size);
    }

}
