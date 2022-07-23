package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;

/**
 * класс для обновления пользователя
 */
@Data
public class UserUpdate {
    private long id;
    private String name;
    @Email
    private String email;
}
