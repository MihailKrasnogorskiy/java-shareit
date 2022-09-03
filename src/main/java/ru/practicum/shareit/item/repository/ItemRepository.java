package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
    /**
     * метод для получения списка id всех вещей
     *
     * @return список id всех вещей
     */
    @Query("select id from Item")
    List<Long> getAllItemsId();

    /**
     * просмотр владельцем списка всех его вещей
     * *
     *
     * @param userId - id владельца вещей
     * @return лист всех вещей пользователя
     */
    List<Item> findByOwner_id(long userId);

    List<Item> findByOwner_id(long userId, Pageable pageable);


    /**
     * метод для текстового поиска вещей по нименованию или описанию без учёта регистра
     *
     * @param text     - текст поиска
     * @param pageable
     * @return лист объектов доступных для аренды вещей, соответствующих запросу
     */
    @Query(" select i from Item as i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) and i.available = true" +
            "   or upper(i.description) like upper(concat('%', ?1, '%')) and i.available = true")
    List<Item> search(String text, Pageable pageable);

    List<Item> findAllByRequestId(long requestId);

}
