package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

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
    private Long requestId;
    private BookingDtoForItemDto lastBooking;
    private BookingDtoForItemDto nextBooking;
    private Set<CommentDto> comments = new HashSet<>();
}