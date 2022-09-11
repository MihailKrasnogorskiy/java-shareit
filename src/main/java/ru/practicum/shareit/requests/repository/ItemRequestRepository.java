package ru.practicum.shareit.requests.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

/**
 * репозиторий запросов вещей
 */
public interface ItemRequestRepository extends CrudRepository<ItemRequest, Long> {
    @Query("select id from ItemRequest")
    List<Long> getAllItemsId();

    List<ItemRequest> findItemRequestByRequester_Id(long requesterId);

    List<ItemRequest> findItemRequestByRequester_IdNot(long requesterId, Pageable pageable);
}
