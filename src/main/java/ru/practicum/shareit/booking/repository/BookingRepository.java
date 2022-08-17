package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Long> {
    List<Booking> findByBooker_id(long bookerId);

    List<Booking> findByBooker_idAndStatus(long bookerId, BookingStatus status);

    @Query(value = "select * from bookings as b join items as i on b.item_id=i.id where i.owner_id = ?1",
            nativeQuery = true)
    List<Booking> findByOwnerId(long ownerId);

    @Query(value = "select * from bookings as b join items as i on b.item_id=i.id where i.owner_id = ?1 and b.status " +
            "= ?2",
            nativeQuery = true)
    List<Booking> findByOwnerIdAndStatus(long ownerId, String status);

    List<Booking> findByItem_id(long itemId);
}
