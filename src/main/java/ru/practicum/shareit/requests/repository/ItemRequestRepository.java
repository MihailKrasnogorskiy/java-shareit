package ru.practicum.shareit.requests.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.requests.ItemRequest;

public interface ItemRequestRepository extends CrudRepository<ItemRequest, Long> {
}
