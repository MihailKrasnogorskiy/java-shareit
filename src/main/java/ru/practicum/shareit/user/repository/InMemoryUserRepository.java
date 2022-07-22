package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * реализация интерфейса репозитория пользователей
 */
@Repository
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private long id = 0;

    @Override
    public void delete(long id) {
        users.removeIf(user -> id == user.getId());
    }

    @Override
    public User getById(long id) {
        return users.stream()
                .filter(user -> id == user.getId())
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User update(long id, UserUpdate userUpdate) {
        if (userUpdate.getEmail() != null) {
            if (!userUpdate.getEmail().equals(getById(id).getEmail())) {
                getById(id).setEmail(userUpdate.getEmail());
            }
        }
        if (userUpdate.getName() != null) {
            if (!userUpdate.getName().equals(getById(id).getName()) && userUpdate.getName() != null) {
                getById(id).setName(userUpdate.getName());
            }
        }
        return getById(id);
    }

    @Override
    public User createUser(User user) {
        user.setId(getId());
        users.add(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public List<String> getAllUsersEmail() {
        return users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getAllUsersId() {
        return users.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    /**
     * метод генерации id пользователея
     *
     * @return id пользователя
     */
    private long getId() {
        return ++id;
    }
}
