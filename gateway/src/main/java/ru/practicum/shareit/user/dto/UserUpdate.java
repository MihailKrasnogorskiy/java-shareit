package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

/**
 * класс для обновления пользователя
 */
@Data
public class UserUpdate {
    private String name;
    @Email
    private String email;
}
