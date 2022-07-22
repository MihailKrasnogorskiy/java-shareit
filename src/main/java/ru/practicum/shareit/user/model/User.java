package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * класс пользователя
 */
@Builder
@Data
public class User {

    private long id;
    @NotNull
    @NotBlank
    private String name;
    @Email
    @NotNull
    private String email;
}
