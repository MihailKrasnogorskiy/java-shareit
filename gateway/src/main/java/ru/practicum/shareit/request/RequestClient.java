package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

/**
 * клиент для отправки запросов в ItemRequestController shareIt-server
 */
@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /**
     * создание запроса
     *
     * @param userId         id пользователя
     * @param itemRequestDto дто объект создания запроса
     * @return дто объект запроса
     */
    public ResponseEntity<Object> create(Long userId, ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    /**
     * поиск всех запросов пользователя
     *
     * @param userId id  пользователя
     * @return список дто объектов всех запросов пользователя
     */
    public ResponseEntity<Object> findAllByUser(Long userId) {
        return get("", userId);
    }

    /**
     * поиск всех запросов других пользователей
     *
     * @param userId id пользователя
     * @param from   начальный элемент выборки
     * @param size   размер выборки
     * @return список всех запросов других пользователей
     */
    public ResponseEntity<Object> findAllOnPage(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

    /**
     * поиск по id
     *
     * @param requestId id запроса
     * @param userId    id  пользователя
     * @return дто объект запроса
     */
    public ResponseEntity<Object> getById(Long userId, Long requestId) {
        return get("/" + requestId, userId);
    }
}
