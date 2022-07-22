package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

/**
 * класс Dto пользователя
 */
@Builder
@Data
public class UserDto {

    private long id;
    private String name;
    private String email;
}