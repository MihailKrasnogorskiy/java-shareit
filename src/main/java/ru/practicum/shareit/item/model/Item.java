package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * класс сдаваемой в аренду вещи
 */
@Data
@Builder
public class Item {
    private long id;
    @NotNull
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Boolean available;
    private long owner;
    private long requestId;
}
