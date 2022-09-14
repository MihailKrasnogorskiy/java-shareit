package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.UserUpdate;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * контроллер пользователей
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * возвращение списка всех пользователей
     *
     * @return список dto объектов пользователей
     */
    @GetMapping
    public List<UserDto> getAll() {
        return service.getAll();
    }

    /**
     * возвращение пользователя по id
     *
     * @param id id пользователя
     * @return dto объект пользователя
     */
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable long id) {
        return service.getById(id);
    }

    /**
     * создание пользователя
     *
     * @param userDto - объект пользователя
     * @return dto объект пользователя
     */
    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return service.create(userDto);
    }

    /**
     * возвращение пользователя по id
     *
     * @param id id пользователя
     * @return dto объект пользователя
     */
    @PatchMapping("/{id}")
    public UserDto update(@PathVariable long id, @RequestBody UserUpdate user) {
        return service.update(id, user);
    }

    /**
     * удадение пользователя
     *
     * @param id - id пользователя
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

}
