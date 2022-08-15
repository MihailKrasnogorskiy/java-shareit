package ru.practicum.shareit.booking.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Long> {
    List<Booking> findByBooker_id(long bookerId);
}
