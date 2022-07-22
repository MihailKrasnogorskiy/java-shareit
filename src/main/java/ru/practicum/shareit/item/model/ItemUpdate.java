package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ItemUpdate {
    private long id;

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
}
