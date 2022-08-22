package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

/**
 * интерфейс репозитория бронирований
 */
public interface BookingRepository extends CrudRepository<Booking, Long> {
    /**
     * поиск по id пользователя создавшего бронирование
     *
     * @param bookerId id пользователя создавшего бронирование
     * @return список бронирований
     */
    List<Booking> findByBooker_id(long bookerId);

    /**
     * поиск по id пользователя создавшего бронирование с учётом статуса бронирования
     *
     * @param bookerId id пользователя создавшего бронирование
     * @param status   статус бронирования
     * @return список бронирований
     */
    List<Booking> findByBooker_idAndStatus(long bookerId, BookingStatus status);

    /**
     * поиск по id владельца вещи
     *
     * @param ownerId id владельца вещи
     * @return список бронирований
     */
    @Query(value = "select * from bookings as b join items as i on b.item_id=i.id where i.owner_id = ?1",
            nativeQuery = true)
    List<Booking> findByOwnerId(long ownerId);

    /**
     * поиск по id владельца вещи с учётом статуса бронирования
     *
     * @param ownerId id владельца вещи
     * @param status  статус бронирования
     * @return список бронирований
     */
    @Query(value = "select * from bookings as b join items as i on b.item_id=i.id where i.owner_id = ?1 and b.status " +
            "= ?2",
            nativeQuery = true)
    List<Booking> findByOwnerIdAndStatus(long ownerId, String status);

    /**
     * поиск по id вещи
     *
     * @param itemId id вещи
     * @return список бронирований
     */
    List<Booking> findByItem_id(long itemId);
}
