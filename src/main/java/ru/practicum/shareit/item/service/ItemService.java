package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemUpdate;

import java.util.List;

public interface ItemService {
    /**
     * просмотр владельцем списка всех его вещей
     * *
     *
     * @param userId - id владельца вещей
     * @return лист dto всех вещей пользователя
     */
    List<ItemDto> getAllByUserId(long userId);

    List<ItemDto> getAllByUserId(long userId, Integer from, Integer size);

    /**
     * поиск вещи по id
     *
     * @param id id вещи
     * @return dto объект вещи
     */
    ItemDto getById(long id, long userId);

    /**
     * метод для текстового поиска вещей по нименованию или описанию без учёта регистра
     *
     * @param text - текст поиска
     * @param from
     * @param size
     * @return лист объектов доступных для аренды вещей, соответствующих запросу
     */
    List<ItemDto> search(String text, Integer from, Integer size);

    /**
     * обновление вещи
     *
     * @param userId     id владельца
     * @param itemId     id обновляемой вещи
     * @param itemUpdate - объект обновляемой вещи
     * @return dto объект обновлённой вещи
     */
    ItemDto update(long userId, long itemId, ItemUpdate itemUpdate);

    /**
     * создание вещи
     *
     * @param userId  - id владельца
     * @param itemDto - dto объект создаваемой вещи
     * @return dto объект созданой вещи
     */
    ItemDto create(long userId, ItemDto itemDto);

    /**
     * удаление вещи владельцем
     *
     * @param userId - id владельца
     * @param itemId - id вещи
     */
    void delete(long userId, long itemId);

    /**
     * добавление комемнтария
     *
     * @param userId  id пользователя
     * @param itemId  id вещи
     * @param comment dto объект комментария
     * @return dto объект комментария
     */
    CommentDto addComment(long userId, long itemId, CommentDto comment);
}
