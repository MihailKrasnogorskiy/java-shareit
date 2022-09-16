package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdate;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    /**
     * создание пользователя
     *
     * @param userDto - объект пользователя
     * @return dto объект пользователя
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto userDto) {
        log.info("User={} has been created", userDto);
        return userClient.create(userDto);
    }

    /**
     * возвращение пользователя по id
     *
     * @param userId id пользователя
     * @return dto объект пользователя
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long userId, @RequestBody UserUpdate updatedUser) {
        log.info("SUser with userId={} has been updated", userId);
        return userClient.update(userId, updatedUser);
    }

    /**
     * возвращение пользователя по id
     *
     * @param userId id пользователя
     * @return dto объект пользователя
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long userId) {
        log.info("Find user by userId={} is successful", userId);
        return userClient.getById(userId);
    }

    /**
     * удадение пользователя
     *
     * @param userId - id пользователя
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long userId) {
        log.info("Delete user by userId={} is successful", userId);
        return userClient.deleteById(userId);
    }

    /**
     * возвращение списка всех пользователей
     *
     * @return список dto объектов пользователей
     */
    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Get all users");
        return userClient.getAll();
    }
}
