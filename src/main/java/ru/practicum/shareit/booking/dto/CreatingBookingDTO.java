package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import java.time.LocalDate;

@Data
@Builder
public class CreatingBookingDTO {
    @Future
    private LocalDate start;
    private LocalDate end;
    private Long item;
    private Long booker;
}
