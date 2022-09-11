package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * дто класс для добавления в дто класс запроса
 */
@Data
@Builder
public class ItemDtoForRequest {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long requestId;
    private long userId;
}
