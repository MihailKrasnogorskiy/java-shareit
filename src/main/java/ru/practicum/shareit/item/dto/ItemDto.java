package ru.practicum.shareit.item.dto;

import lombok.Builder;
import ru.practicum.shareit.user.User;

/**
 * // TODO .
 */
@Builder
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private long requestId;
}
