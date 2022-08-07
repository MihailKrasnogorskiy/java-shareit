package ru.practicum.shareit.booking.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepository extends CrudRepository<Booking, Long> {
}
