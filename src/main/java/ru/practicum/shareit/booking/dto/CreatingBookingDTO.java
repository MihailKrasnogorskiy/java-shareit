package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Data
@Builder
public class CreatingBookingDTO {
    @FutureOrPresent
    private LocalDate start;
    private LocalDate end;
    private Long itemId;
    private Long booker;
}
