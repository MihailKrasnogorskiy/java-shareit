package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * интерфейс репозитория пользователей
 */
public interface UserRepository extends CrudRepository<User, Long> {
    @Transactional
    @Query("select u.id from User as u")
    List<Long> getAllUsersId();

    @Transactional
    @Query("select u.email from User as u")
    List<String> getAllUsersEmail();
}
