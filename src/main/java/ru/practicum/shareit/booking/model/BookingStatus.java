package ru.practicum.shareit.booking.model;

import lombok.ToString;

@ToString
public enum BookingStatus {
    WAITING, APPROVED, REJECTED, CANCELED
}
