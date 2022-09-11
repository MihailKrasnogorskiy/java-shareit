package ru.practicum.shareit.requests.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.ArrayList;

/**
 * маппер запросов вещи
 */
@Component
public class ItemRequestMapper {

    /**
     * преодразование объекта запроса в dto объект запроса
     *
     * @param request объект запроса
     * @returndto объект запроса
     */
    public ItemRequestDto toItemRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .items(new ArrayList<>())
                .build();
    }
}
