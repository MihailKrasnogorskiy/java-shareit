package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

// класс dto объекта комментария
@Data
@Builder
public class CommentDto {
    private long id;
    @NotBlank
    private String text;
    private String authorName;
    private LocalDateTime created;
}
