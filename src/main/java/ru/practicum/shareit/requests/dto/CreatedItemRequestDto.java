package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
public class CreatedItemRequestDto {
    @NotBlank
    private String description;
}
