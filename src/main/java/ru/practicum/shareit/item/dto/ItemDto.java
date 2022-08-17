package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * класс Dto сдаваемой в аренду вещи
 */
@Builder
@Data
@AllArgsConstructor
public class ItemDto {
    private long id;
    @NotNull
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private long owner;
    private long requestId;
    private BookingDtoForItemDto lastBooking;
    private BookingDtoForItemDto nextBooking;
}