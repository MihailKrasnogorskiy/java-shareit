package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

/**
 * класс для обновления пользователя
 */
@Data
public class UserUpdateDto {
    private String name;
    @Email
    private String email;
}
