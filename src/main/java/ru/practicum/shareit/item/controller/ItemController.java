package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemUpdate;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * контроллер
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    /**
     * просмотр владельцем списка всех его вещей
     * *
     *
     * @param userId - id владельца вещей
     * @return лист dto всех вещей пользователя
     */
    @GetMapping
    public List<ItemDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getAllByUserId(userId);
    }

    /**
     * поиск вещи по id
     *
     * @param itemId id вещи
     * @return dto объект вещи
     */
    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable long itemId) {
        return service.getById(itemId);
    }

    /**
     * обновление вещи
     *
     * @param userId     id владельца
     * @param itemId     id обновляемой вещи
     * @param itemUpdate - объект обновляемой вещи
     * @return dto объект обновлённой вещи
     */
    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                          @RequestBody ItemUpdate itemUpdate) {
        return service.update(userId, itemId, itemUpdate);
    }

    /**
     * создание вещи
     *
     * @param userId  - id владельца
     * @param itemDto - dto объект создаваемой вещи
     * @return dto объект созданой вещи
     */
    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        return service.create(userId, itemDto);
    }

    /**
     * метод для текстового поиска вещей по нименованию или описанию без учёта регистра
     *
     * @param text - текст поиска
     * @return лист dto объектов доступных для аренды вещей, соответствующих запросу
     */
    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return service.search(text);
    }
}
