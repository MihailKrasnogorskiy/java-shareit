package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

/**
 * класс для создания Dto объекта пользователя из стандартного объекта пользователя
 */
@Component
public class UserMapper {
    /**
     * метод для создания Dto объекта пользователя из стандартного объекта пользователя
     *
     * @param user - объект пользователя
     * @return - Dto объект
     */
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    /**
     * метод для создания объекта пользователя из Dto объекта
     *
     * @param userDto - dto объект
     * @return - объект пользователя
     */
    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
