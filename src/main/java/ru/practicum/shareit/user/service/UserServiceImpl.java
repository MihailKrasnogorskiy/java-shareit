package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailUsedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserUpdate;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * реализация интерфейса сервиса пользователей
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return repository.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(User user) {
        validateUserEmail(user.getEmail());
        return UserMapper.toUserDto(repository.createUser(user));
    }

    @Override
    public UserDto update(long id, UserUpdate user) {
        validateUserId(id);
        if (user.getEmail() != null && !user.getEmail().equals(repository.getById(id).getEmail())) {
            validateUserEmail(user.getEmail());
        }
        return UserMapper.toUserDto(repository.update(id, user));
    }

    @Override
    public UserDto getById(long id) {
        validateUserId(id);
        return UserMapper.toUserDto(repository.getById(id));
    }

    @Override
    public void deleteUser(long id) {
        validateUserId(id);
        repository.delete(id);
    }

    /**
     * метод проверки существования пользователя с заданым id
     *
     * @param id - id пользователя из запроса
     */
    @Override
    public void validateUserId(long id) {
        if (!repository.getAllUsersId().contains(id)) {
            throw new NotFoundException("User with id " + id + " not found");
        }
    }

    /**
     * метод проверки регистрации другого пользователя на уже зарегистрированный email
     *
     * @param email - email адресс из запроса
     */
    private void validateUserEmail(String email) {
        if (repository.getAllUsersEmail().contains(email)) {
            throw new EmailUsedException("User with email " + email + " already use");
        }
    }
}
