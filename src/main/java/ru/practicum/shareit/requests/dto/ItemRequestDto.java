package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
@Builder
@AllArgsConstructor
public class ItemRequestDto {
    private long id;
    private String description;
    private long requester;
    private LocalDateTime created;
}
