package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * класс объекта обновления сдаваемой в аренду вещи
 */
@Data
@Builder
public class ItemUpdate {
    private String name;
    private String description;
    private Boolean available;
}
