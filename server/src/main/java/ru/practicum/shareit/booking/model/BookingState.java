package ru.practicum.shareit.booking.model;

/**
 * перечисление вариантов выборки бронирований
 */
public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, UNSUPPORTED_STATUS, REJECTED
}
