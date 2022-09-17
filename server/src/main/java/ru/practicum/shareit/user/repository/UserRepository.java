package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * интерфейс репозитория пользователей
 */
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("select u.id from User as u")
    List<Long> getAllUsersId();


    @Query("select u.email from User as u")
    List<String> getAllUsersEmail();

    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE users ALTER COLUMN id RESTART WITH 1", nativeQuery = true)
    void clearIncrement();

    @Transactional
    @Modifying
    @Query(value = "DROP TABLE IF EXISTS users CASCADE", nativeQuery = true)
    void clearTable();
}
