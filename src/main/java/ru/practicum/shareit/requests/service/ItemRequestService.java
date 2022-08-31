package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.CreatedItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(long userId, CreatedItemRequestDto description);

    ItemRequestDto getById(long requestId);

    List<ItemRequestDto> findAllByUser(long userId);

    List<ItemRequestDto> findAllOnPage(long userId, Integer from, Integer size);
}
