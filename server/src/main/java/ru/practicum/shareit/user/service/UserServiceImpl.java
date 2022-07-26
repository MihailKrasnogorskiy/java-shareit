package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailUsedException;
import ru.practicum.shareit.exception.IsBlankException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserUpdate;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * реализация интерфейса сервиса пользователей
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDto> getAll() {
        List<User> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        List<UserDto> list = result.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("All users list has been returned");
        return list;
    }

    @Override
    public UserDto create(UserDto userDto) {
        userDto.setId(0L);
        try {
            UserDto userdto = UserMapper.toUserDto(repository.save(UserMapper.toUser(userDto)));
            log.info("User with id {} has been created", userdto.getId());
            return userdto;
        } catch (DataIntegrityViolationException e) {
            throw new EmailUsedException("This email is already use");
        }
    }

    @Override
    public UserDto update(Long id, UserUpdate user) {
        validateUserId(id);
        if (user.getEmail() != null) {
            validateUserEmail(user.getEmail());
        }
        User userInDB = repository.findById(id).get();
        if (user.getName() != null) {
            if (user.getName().isBlank()) {
                throw new IsBlankException("Username");
            }
            userInDB.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userInDB.setEmail(user.getEmail());
        }
        UserDto userdto = UserMapper.toUserDto(repository.save(userInDB));
        log.info("User with id {} has been updated", userdto.getId());
        return userdto;
    }

    @Override
    public UserDto getById(long id) {
        validateUserId(id);
        UserDto dto = UserMapper.toUserDto(repository.findById(id).get());
        log.info("User with id {} has been returned", id);
        return dto;
    }

    @Override
    public void delete(long id) {
        validateUserId(id);
        repository.deleteById(id);
        log.info("User with id {} has been deleted", id);
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
