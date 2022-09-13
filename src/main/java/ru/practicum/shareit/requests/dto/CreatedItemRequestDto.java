package ru.practicum.shareit.requests.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * класс дто объекта для создания запроса вещи
 */
@Data
public class CreatedItemRequestDto {
    @NotBlank
    private String description;
}
