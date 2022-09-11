package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * dto класс запроса вещи
 */
@Data
@Builder
@AllArgsConstructor
public class ItemRequestDto {
    private long id;
    private String description;
    private long requester;
    private LocalDateTime created;
    private List<ItemDtoForRequest> items;
}
