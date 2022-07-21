package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

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
    public List<UserDto> getAllUsers() {
        return service.getAllUsers();
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
     * @param user - объект пользователя
     * @return dto объект пользователя
     */
    @PostMapping
    public UserDto createUser(@Valid @RequestBody User user) {
        return service.createUser(user);
    }

    /**
     * возвращение пользователя по id
     *
     * @param id id пользователя
     * @return dto объект пользователя
     */
    @PatchMapping("/{id}")
    public UserDto updateUser(@Valid @PathVariable long id, @RequestBody UserUpdate user) {
        return service.update(id, user);
    }

    /**
     * удадение пользователя
     *
     * @param id - id пользователя
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        service.deleteUser(id);
    }

}
