package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
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
    public List<ItemDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "20") Integer size) {
        return service.getAllByUserId(userId, from, size);
    }

    /**
     * поиск вещи по id
     *
     * @param itemId id вещи
     * @return dto объект вещи
     */
    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        return service.getById(itemId, userId);
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
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId, @Valid
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
    public List<ItemDto> search(@RequestParam String text,
                                @RequestParam(defaultValue = "0") Integer from,
                                @RequestParam(defaultValue = "20") Integer size) {
        return service.search(text, from, size);
    }

    /**
     * удаление вещи владельцем
     *
     * @param userId - id владельца
     * @param itemId - id вещи
     */
    @DeleteMapping("{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        service.delete(userId, itemId);
    }

    /**
     * добавление комемнтария
     *
     * @param userId  id пользователя
     * @param itemId  id вещи
     * @param comment dto объект комментария
     * @return dto объект комментария
     */
    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                                 @Valid @RequestBody CommentDto comment) {
        return service.addComment(userId, itemId, comment);

    }
}
