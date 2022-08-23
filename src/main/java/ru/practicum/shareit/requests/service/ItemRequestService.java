package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.CreatedItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

public interface ItemRequestService {
   ItemRequestDto create(long userId, CreatedItemRequestDto description);
}
