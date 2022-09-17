package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * dto класс запроса вещи
 */
@Data
@Builder
@AllArgsConstructor
public class ItemRequestDto {
    @NotBlank
    private String description;
    @FutureOrPresent
    private LocalDateTime created;
}
