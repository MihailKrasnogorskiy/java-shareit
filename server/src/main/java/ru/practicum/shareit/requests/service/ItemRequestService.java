package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.CreatedItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;

/**
 * сервис запросов вещей
 */
public interface ItemRequestService {
    /**
     * создание запроса
     *
     * @param userId      id пользователя
     * @param description дто объект создания запроса
     * @return дто объект запроса
     */
    ItemRequestDto create(long userId, CreatedItemRequestDto description);

    /**
     * поиск по id
     *
     * @param requestId id запроса
     * @param userId    id  пользователя
     * @return дто объект запроса
     */
    ItemRequestDto getById(long requestId, long userId);

    /**
     * поиск всех запросов пользователя
     *
     * @param userId id  пользователя
     * @return список дто объектов всех запросов пользователя
     */
    List<ItemRequestDto> findAllByUser(long userId);

    /**
     * поиск всех запросов других пользователей
     *
     * @param userId id пользователя
     * @param from   начальный элемент выборки
     * @param size   размер выборки
     * @return список дто объекто всех запросов других пользователей
     */
    List<ItemRequestDto> findAllOnPage(long userId, Integer from, Integer size);
}
