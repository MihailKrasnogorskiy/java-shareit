package ru.practicum.shareit.requests.dto;

import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
public class ItemRequestDto {
    private long id;
    private String description;
    private User requester;
    private LocalDateTime created;
}
