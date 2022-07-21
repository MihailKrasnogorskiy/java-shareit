package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

/**
 * // TODO .
 */
public class BookingDto {
    private long id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private User booker;
    private String status;
}
