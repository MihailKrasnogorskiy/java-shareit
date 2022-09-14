package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * класс dto объкта создания бронирования
 */
@Data
@Builder
public class CreatingBookingDto {

    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long booker;
}
