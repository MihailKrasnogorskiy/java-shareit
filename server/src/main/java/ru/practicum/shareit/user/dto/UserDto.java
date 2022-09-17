package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * класс Dto пользователя
 */
@Builder
@Data
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
}