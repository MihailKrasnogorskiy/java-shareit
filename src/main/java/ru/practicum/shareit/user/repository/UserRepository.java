package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserUpdate;

import java.util.List;

/**
 * интерфейс репозитория пользователей
 */
public interface UserRepository {
    /**
     * удаление пользователя
     *
     * @param id - id пользователя
     */
    void delete(long id);

    /**
     * возвращение пользователя по id
     *
     * @param id - id пользователя
     * @return объект пользователя
     */
    User getById(long id);

    /**
     * обновление пользователя
     *
     * @param id   - id пользователя
     * @param user - объект пользователя для обновления
     * @return - объект пользователя после обновления
     */
    User update(long id, UserUpdate user);

    /**
     * создание пользователя
     *
     * @param user - объект пользователя
     * @return объект пользователя
     */
    User createUser(User user);

    /**
     * возвращение списка всех пользователей
     *
     * @return список всех пользователей
     */
    List<User> getAllUsers();

    /**
     * возвращает список всех имэйлов пользователей
     *
     * @return список всех имэйлов пользователей
     */
    List<String> getAllUsersEmail();

    /**
     * возвращает список всех id пользователей
     *
     * @return список всех id пользователей
     */
    List<Long> getAllUsersId();

    /**
     * очищает хранилище и сбрасывает счётчик
     */
    void clear();
}
