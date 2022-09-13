package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.UserUpdate;

import java.util.List;

/**
 * интерфейс сервиса пользователей
 */
public interface UserService {
    /**
     * возвращение списка всех пользователей
     *
     * @return список dto объектов пользователей
     */
    List<UserDto> getAll();

    /**
     * создание пользователя
     *
     * @param userDto - объект пользователя
     * @return dto объект пользователя
     */
    UserDto create(UserDto userDto);

    /**
     * обновление пользователя
     *
     * @param user - объект пользователя
     * @return dto объект пользователя
     */
    UserDto update(Long id, UserUpdate user);

    /**
     * возвращение пользователя по id
     *
     * @param id id пользователя
     * @return dto объект пользователя
     */
    UserDto getById(long id);

    /**
     * удадение пользователя
     *
     * @param id - id пользователя
     */
    void delete(long id);

    void validateUserId(long id);
}
