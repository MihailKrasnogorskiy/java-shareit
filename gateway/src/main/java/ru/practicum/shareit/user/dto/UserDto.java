package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * класс Dto пользователя
 */
@Builder
@Data
@AllArgsConstructor
public class UserDto {
    @NotNull
    @NotBlank
    private String name;
    @Email
    @NotNull
    private String email;
}