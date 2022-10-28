package java.ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.OffsetLimitPageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * тестовый класс репозитория бронирований
 */
@DataJpaTest
@Transactional
public class BookingRepositoryTest {
    private final User user = new User();
    private final User user1 = new User();
    private final Item item = new Item();
    private final Booking booking = new Booking();
    private final Pageable pageable = OffsetLimitPageable.of(0, 20, Sort.unsorted());
    @Autowired
    TestEntityManager em;
    @Autowired
    BookingRepository bookingRepository;

    /**
     * создание окружения
     */
    @BeforeEach
    void createEnvironment() {
        user.setName("Vova");
        user.setEmail("vova@mail.com");
        em.persist(user);

        item.setName("test");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);
        em.persist(item);

        user1.setName("Vika");
        user1.setEmail("vika@vikamail.com");
        em.persist(user1);

        booking.setStart(LocalDateTime.now().plusDays(2));
        booking.setEnd(LocalDateTime.now().plusDays(5));
        booking.setItem(item);
        booking.setBooker(user1);
        booking.setStatus(BookingStatus.APPROVED);
        em.persist(booking);
    }

    /**
     * получение всех бронирований владельца вещи
     */
    @Test
    void test12_findAllByOwnerIdOrderByEndDesc() {

        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(user.getId(), pageable).stream()
                .collect(Collectors.toList());
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(booking, bookings.get(0));
    }

    /**
     * получение всех бронирований владельца вещи с указанием статуса бронирования
     */
    @Test
    void test13_findAllByOwnerIdAndStatusOrderByEndDesc() {

        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(user.getId(),
                BookingStatus.APPROVED, pageable).stream().collect(Collectors.toList());
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(booking, bookings.get(0));
    }

    /**
     * получение всех бронирований пользователя
     */
    @Test
    void test14_findAllByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(user1.getId(), pageable).stream()
                .collect(Collectors.toList());
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(booking, bookings.get(0));
    }

    /**
     * получение всех бронирований пользователя с указанием статуса бронирования
     */
    @Test
    void test15_findAllByBookerIdAndStatusOrderByStartDesc() {

        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(user1.getId(),
                BookingStatus.APPROVED, pageable).stream().collect(Collectors.toList());
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(booking, bookings.get(0));
    }

}