package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * класс Dto сдаваемой в аренду вещи
 */
@Builder
@Data
@AllArgsConstructor
public class ItemDto {
    @NotNull
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private long owner;
    private Long requestId;
}