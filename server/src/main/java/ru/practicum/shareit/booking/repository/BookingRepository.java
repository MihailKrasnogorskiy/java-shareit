package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * интерфейс репозитория бронирований
 */
public interface BookingRepository extends CrudRepository<Booking, Long> {

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end);

    Page<Booking> findAllByBookerIdOrderByStartDesc(long userId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime start,
                                                                             LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long userId, BookingStatus status, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdOrderByStartDesc(long userId, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime start,
                                                                                LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, BookingStatus status, Pageable pageable);

    /**
     * поиск по id вещи
     *
     * @param itemId id вещи
     * @return список бронирований
     */
    List<Booking> findByItem_id(long itemId);
}
