package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Future;
import java.time.LocalDate;

/**
 * // TODO .
 */
@Data
@Builder
public class BookingDto {
    private long id;
    @Future
    private LocalDate start;
    private LocalDate end;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
