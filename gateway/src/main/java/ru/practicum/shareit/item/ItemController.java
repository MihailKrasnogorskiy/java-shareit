package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * контроллер вещей
 */
@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    @Autowired
    private ItemClient itemClient;

    /**
     * просмотр пользователем всех его вещей
     *
     * @param userId - id пользователя
     * @param from   - начальное значение выборки
     * @param size   - размер выборки
     * @return список dto-объектов вещей
     */
    @GetMapping
    public ResponseEntity<Object> findAllByUser(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get items with userId={}, from={}, size={}", userId, from, size);
        return itemClient.findAllByUser(userId, from, size);
    }

    /**
     * поиск вещи по id
     *
     * @param itemId id вещи
     * @return dto объект вещи
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                          @Positive @PathVariable Long itemId) {
        log.info("Get item {}, userId={}", itemId, userId);
        return itemClient.getById(userId, itemId);
    }

    /**
     * создание вещи
     *
     * @param userId  - id владельца
     * @param itemDto - dto объект создаваемой вещи
     * @return dto объект созданой вещи
     */
    @PostMapping
    public ResponseEntity<Object> createNewItem(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Valid ItemDto itemDto) {
        log.info("Item {} has been created", itemDto);
        return itemClient.create(userId, itemDto);
    }

    /**
     * обновление вещи
     *
     * @param userId  id владельца
     * @param itemId  id обновляемой вещи
     * @param itemDto - объект обновляемой вещи
     * @return dto объект обновлённой вещи
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody ItemUpdate itemDto,
                                             @Positive @PathVariable("itemId") Long itemId) {
        log.info("Item with id={} has been updated", itemId);
        return itemClient.update(userId, itemDto, itemId);
    }

    /**
     * метод для текстового поиска вещей по нименованию или описанию без учёта регистра
     *
     * @param text - текст поиска
     * @param from - начальное значение выборки
     * @param size - размер выборки
     * @return лист dto объектов доступных для аренды вещей, соответствующих запросу
     */
    @GetMapping("/search")
    public ResponseEntity<Object> search(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestParam(name = "text", defaultValue = "") String text,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Search item by text={}, userId={} is successful", text, userId);
        return itemClient.search(userId, text, from, size);
    }

    /**
     * добавление комемнтария
     *
     * @param userId     id пользователя
     * @param itemId     id вещи
     * @param commentDto dto объект комментария
     * @return dto объект комментария
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Positive @PathVariable("itemId") Long itemId,
                                             @RequestBody @Valid CommentDto commentDto) {
        log.info("Comment for itemId={}, userId={} has been created", itemId, userId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
