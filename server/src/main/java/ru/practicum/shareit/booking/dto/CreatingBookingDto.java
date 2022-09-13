package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

/**
 * класс dto объкта создания бронирования
 */
@Data
@Builder
public class CreatingBookingDto {
    @FutureOrPresent
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long booker;
}
