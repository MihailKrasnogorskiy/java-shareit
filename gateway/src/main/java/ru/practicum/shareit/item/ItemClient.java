package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdate;

import java.util.Map;

/**
 * клиент для отправки запросов в ItemController shareIt-server
 */
@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /**
     * просмотр пользователем всех его вещей
     *
     * @param userId - id пользователя
     * @param from   - начальное значение выборки
     * @param size   - размер выборки
     * @return список dto-объектов вещей
     */
    public ResponseEntity<Object> findAllByUser(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    /**
     * поиск вещи по id
     *
     * @param itemId id вещи
     * @return dto объект вещи
     */
    public ResponseEntity<Object> getById(long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    /**
     * создание вещи
     *
     * @param userId  - id владельца
     * @param itemDto - dto объект создаваемой вещи
     * @return dto объект созданой вещи
     */
    public ResponseEntity<Object> create(Long userId, ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    /**
     * обновление вещи
     *
     * @param userId  id владельца
     * @param itemId  id обновляемой вещи
     * @param itemDto - объект обновляемой вещи
     * @return dto объект обновлённой вещи
     */

    public ResponseEntity<Object> update(Long userId, ItemUpdate itemDto, Long itemId) {
        return patch("/" + itemId, userId, itemDto);
    }

    /**
     * метод для текстового поиска вещей по нименованию или описанию без учёта регистра
     *
     * @param text - текст поиска
     * @param from - начальное значение выборки
     * @param size - размер выборки
     * @return лист dto объектов доступных для аренды вещей, соответствующих запросу
     */
    public ResponseEntity<Object> search(Long userId, String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    /**
     * добавление комемнтария
     *
     * @param userId  id пользователя
     * @param itemId  id вещи
     * @param commentDto dto объект комментария
     * @return dto объект комментария
     */
    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
