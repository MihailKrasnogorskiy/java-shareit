package ru.practicum.shareit.requests.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.requests.ItemRequest;
@Component
public class ItemRequestMapper {
public ItemRequestDto toItemRequestDto(ItemRequest request){
    return ItemRequestDto.builder()
            .id(request.getId())
            .description(request.getDescription())
            .requester(request.getRequester().getId())
            .created(request.getCreated())
            .build();
}
}
