package ru.practicum.shareit.requests.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * // TODO .
 */
@Data
public class CreatedItemRequestDto {
    @NotBlank
    private String description;
}
