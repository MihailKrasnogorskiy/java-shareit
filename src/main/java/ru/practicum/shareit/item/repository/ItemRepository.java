package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemUpdate;

import java.util.List;

public interface ItemRepository {

    /**
     * создание вещи
     *
     * @param userId - id владельца
     * @param item   - объект создаваемой вещи
     * @return - объект созданой вещи
     */
    Item create(long userId, Item item);

    /**
     * метод для получения списка id всех вещей
     *
     * @return список id всех вещей
     */
    List<Long> getAllItemsId();

    /**
     * поиск вещи по id
     *
     * @param id id вещи
     * @return объект вещи
     */
    Item getById(long id);

    /**
     * просмотр владельцем списка всех его вещей
     * *
     *
     * @param userId - id владельца вещей
     * @return лист всех вещей пользователя
     */
    List<Item> getAllByUserId(long userId);

    /**
     * обновление вещи
     *
     * @param userId     id владельца
     * @param itemId     id обновляемой вещи
     * @param itemUpdate - объект обновляемой вещи
     * @return объект обновлённой вещи
     */
    Item update(long userId, long itemId, ItemUpdate itemUpdate);

    /**
     * метод для текстового поиска вещей по нименованию или описанию без учёта регистра
     *
     * @param text - текст поиска
     * @return лист объектов доступных для аренды вещей, соответствующих запросу
     */
    List<Item> search(String text);

    /**
     * удаление вещи владельцем
     *
     * @param userId - id владельца
     * @param itemId - id вещи
     */
    void delete(long userId, long itemId);

    /**
     * очищает хранилище и сбрасывает счётчик
     */
    void clear();
}
