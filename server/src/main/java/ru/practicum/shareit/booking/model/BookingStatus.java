package ru.practicum.shareit.booking.model;

import lombok.ToString;

/**
 * перечисление статусов бронирований
 */
@ToString
public enum BookingStatus {
    WAITING, APPROVED, REJECTED, CANCELED
}
