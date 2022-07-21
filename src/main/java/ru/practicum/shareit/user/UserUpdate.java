package ru.practicum.shareit.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * класс для обновления пользователя
 */
@Data
public class UserUpdate {
    private long id;
    @NotBlank
    private String name;
    @Email
    private String email;


}
