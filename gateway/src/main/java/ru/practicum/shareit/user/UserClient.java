package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

/**
 * клиент для отправки запросов в UserController shareIt-server
 */
@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /**
     * создание пользователя
     *
     * @param userDto - объект пользователя
     * @return dto объект пользователя
     */
    public ResponseEntity<Object> create(UserDto userDto) {
        return post("", userDto);
    }

    /**
     * возвращение пользователя по id
     *
     * @param userId id пользователя
     * @return dto объект пользователя
     */
    public ResponseEntity<Object> update(long userId, UserUpdateDto userDto) {
        return patch("/" + userId, userDto);
    }

    /**
     * возвращение пользователя по id
     *
     * @param userId id пользователя
     * @return dto объект пользователя
     */
    public ResponseEntity<Object> getById(long userId) {
        return get("/" + userId);
    }

    /**
     * удадение пользователя
     *
     * @param userId - id пользователя
     */
    public ResponseEntity<Object> deleteById(long userId) {
        return delete("/" + userId);
    }

    /**
     * возвращение списка всех пользователей
     *
     * @return список dto объектов пользователей
     */
    public ResponseEntity<Object> getAll() {
        return get("");
    }
}
