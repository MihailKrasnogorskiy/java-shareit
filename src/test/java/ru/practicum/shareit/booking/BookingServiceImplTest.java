package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreatingBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemUpdate;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingServiceImplTest {
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingService bookingService;
    LocalDateTime start = LocalDateTime.now().withSecond(0).withNano(0).plusDays(1);
    LocalDateTime end = start.plusDays(1);

    private CreatingBookingDto creatingDto = CreatingBookingDto.builder()
            .booker(2L)
            .itemId(1L)
            .start(start)
            .end(end)
            .build();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void test21_create() {
        BookingDto dto = bookingService.create(2L, creatingDto);
        assertEquals(1, dto.getId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
        assertEquals(1, dto.getItem().getId());
        assertEquals(1, dto.getItem().getOwner());
        assertEquals(2, dto.getBooker().getId());
        assertEquals(BookingStatus.WAITING, dto.getStatus());
        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            bookingService.create(1L, creatingDto);
        });
        assertEquals("You cannot book your item", thrown.getMessage());
        thrown = assertThrows(NotFoundException.class, () -> {
            bookingService.create(99L, creatingDto);
        });
        assertNotNull(thrown.getMessage());
        creatingDto.setItemId(99L);
        thrown = assertThrows(NotFoundException.class, () -> {
            bookingService.create(2L, creatingDto);
        });
        assertNotNull(thrown.getMessage());
        ItemUpdate itemDto = ItemUpdate.builder()
                .available(false)
                .build();
        itemService.update(1L, 1L, itemDto);
        creatingDto.setItemId(1L);
        thrown = assertThrows(ItemUnavailableException.class, () -> {
            bookingService.create(2L, creatingDto);
        });
        assertEquals("This item is not available", thrown.getMessage());
    }

    @Test
    void test22_approve() {
        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            bookingService.approve(1, false, 1);
        });
        assertEquals("This booking not found", thrown.getMessage());
        bookingService.create(2, creatingDto);
        BookingDto dto = bookingService.approve(1, true, 1);
        assertEquals(1, dto.getId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
        assertEquals(1, dto.getItem().getId());
        assertEquals(1, dto.getItem().getOwner());
        assertEquals(2, dto.getBooker().getId());
        assertEquals(BookingStatus.APPROVED, dto.getStatus());
        thrown = assertThrows(BookingApprovedException.class, () -> {
            bookingService.approve(1, false, 1);
        });
        assertEquals("You have already replied to this booking", thrown.getMessage());
        thrown = assertThrows(OwnerValidationException.class, () -> {
            bookingService.approve(2, false, 1);
        });
        assertEquals("You are not item owner", thrown.getMessage());
        thrown = assertThrows(NotFoundException.class, () -> {
            bookingService.approve(3, false, 1);
        });
        assertNotNull(thrown.getMessage());
        clearEnvironment();
        createEnvironment();
        bookingService.create(2, creatingDto);
        dto = bookingService.approve(1, false, 1);
        assertEquals(1, dto.getId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
        assertEquals(1, dto.getItem().getId());
        assertEquals(1, dto.getItem().getOwner());
        assertEquals(2, dto.getBooker().getId());
        assertEquals(BookingStatus.REJECTED, dto.getStatus());
    }

    @Test
    void test23_validationEndDate() {
        creatingDto.setEnd(start);
        creatingDto.setStart(end);
        Throwable thrown = assertThrows(EndDateValidateException.class, () -> {
            bookingService.create(2, creatingDto);
        });
        assertEquals("EndDate is before StartDate", thrown.getMessage());
        creatingDto.setStart(start);
        creatingDto.setEnd(end);
    }

    @Test
    void test24_findById() {
        bookingService.create(2, creatingDto);
        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            bookingService.findById(3, 1);
        });
        assertNotNull(thrown.getMessage());
        BookingDto dto = bookingService.findById(2, 1);
        assertEquals(1, dto.getId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
        assertEquals(1, dto.getItem().getId());
        assertEquals(1, dto.getItem().getOwner());
        assertEquals(2, dto.getBooker().getId());
        assertEquals(BookingStatus.WAITING, dto.getStatus());
        dto = bookingService.findById(1, 1);
        assertEquals(1, dto.getId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
        assertEquals(1, dto.getItem().getId());
        assertEquals(1, dto.getItem().getOwner());
        assertEquals(2, dto.getBooker().getId());
        assertEquals(BookingStatus.WAITING, dto.getStatus());
        UserDto user = UserDto.builder()
                .name("Vika")
                .email("vika@mail.ru")
                .build();
        userService.create(user);
        thrown = assertThrows(BookerValidationException.class, () -> {
            bookingService.findById(3, 1);
        });
        assertEquals("You are not booker", thrown.getMessage());
    }

    @Test
    void test26_findAllByUser() {
        createBookings();
        List<BookingDto> list = bookingService.findAllByUser(2L, BookingState.ALL, 0, 10);
        assertEquals(4, list.size());
        list.clear();
        list = bookingService.findAllByUser(2L, BookingState.PAST, 0, 10);
        assertEquals(1, list.size());
        assertEquals(4, list.get(0).getId());
        list.clear();
        list = bookingService.findAllByUser(2L, BookingState.CURRENT, 0, 10);
        assertEquals(1, list.size());
        assertEquals(3, list.get(0).getId());
        list.clear();
        list = bookingService.findAllByUser(2L, BookingState.REJECTED, 0, 10);
        assertEquals(1, list.size());
        assertEquals(4, list.get(0).getId());
        list.clear();
        list = bookingService.findAllByUser(2L, BookingState.FUTURE, 0, 10);
        assertEquals(2, list.size());
        assertEquals(2, list.get(0).getId());
        list.clear();
        list = bookingService.findAllByUser(2L, BookingState.WAITING, 0, 10);
        assertEquals(3, list.size());
        assertEquals(2, list.get(0).getId());
        Throwable thrown = assertThrows(PageArgsValidationException.class, () -> {
            bookingService.findAllByUser(2L, BookingState.ALL, -1, 0);
        });
        assertEquals("from must be positive and size must be more then 0", thrown.getMessage());
    }

    @Test
    void test27_findAllByOwner() {
        createBookings();
        List<BookingDto> list = bookingService.findAllByOwner(1L, BookingState.ALL, 0, 10);
        assertEquals(4, list.size());
        list.clear();
        list = bookingService.findAllByOwner(1L, BookingState.PAST, 0, 10);
        assertEquals(1, list.size());
        assertEquals(4, list.get(0).getId());
        list.clear();
        list = bookingService.findAllByOwner(1L, BookingState.CURRENT, 0, 10);
        assertEquals(1, list.size());
        assertEquals(3, list.get(0).getId());
        list.clear();
        list = bookingService.findAllByOwner(1L, BookingState.REJECTED, 0, 10);
        assertEquals(1, list.size());
        assertEquals(4, list.get(0).getId());
        list.clear();
        list = bookingService.findAllByOwner(1L, BookingState.FUTURE, 0, 10);
        assertEquals(2, list.size());
        assertEquals(2, list.get(0).getId());
        list.clear();
        list = bookingService.findAllByOwner(1L, BookingState.WAITING, 0, 10);
        assertEquals(3, list.size());
        assertEquals(2, list.get(0).getId());
    }

    @BeforeEach
    void createEnvironment() {
        clearEnvironment();
        UserDto user = UserDto.builder()
                .name("Voldemar")
                .email("voldemar@mail.ru")
                .build();
        userService.create(user);
        user.setName("Ivan");
        user.setEmail("ivan@ivan.ru");
        userService.create(user);
        ItemDto itemDto = ItemDto.builder()
                .name("Машина")
                .description("Audi TT")
                .available(true)
                .owner(1)
                .comments(new HashSet<>())
                .build();
        itemService.create(1L, itemDto);
    }

    @AfterEach
    void clearEnvironment() {
        String query = "SET REFERENTIAL_INTEGRITY = FALSE";
        jdbcTemplate.update(query);
        query = "TRUNCATE TABLE users ";
        jdbcTemplate.update(query);
        query = "ALTER TABLE users ALTER COLUMN id RESTART WITH 1";
        jdbcTemplate.update(query);
        query = "TRUNCATE TABLE items ";
        jdbcTemplate.update(query);
        query = "ALTER TABLE items ALTER COLUMN id RESTART WITH 1";
        jdbcTemplate.update(query);
        query = "TRUNCATE TABLE bookings ";
        jdbcTemplate.update(query);
        query = "ALTER TABLE bookings ALTER COLUMN id RESTART WITH 1";
        jdbcTemplate.update(query);
        query = "SET REFERENTIAL_INTEGRITY = TRUE";
        jdbcTemplate.update(query);
    }

    private void createBookings() {
        ItemDto itemDto = ItemDto.builder()
                .name("Лодка")
                .description("Нырок")
                .available(true)
                .owner(1)
                .comments(new HashSet<>())
                .build();
        itemService.create(1L, itemDto);
        bookingService.create(2L, creatingDto);
        creatingDto.setItemId(2L);
        start = start.plusHours(1);
        creatingDto.setStart(start);
        bookingService.create(2L, creatingDto);
        start = LocalDateTime.now().minusDays(2);
        end = LocalDateTime.now().minusDays(1);
        creatingDto.setStart(start);
        bookingService.create(2L, creatingDto);
        creatingDto.setItemId(1L);
        creatingDto.setEnd(end);
        bookingService.create(2L, creatingDto);
        bookingService.approve(1, false, 4);
        creatingDto.setItemId(1L);
        creatingDto.setStart(LocalDateTime.now().plusDays(1L));
        creatingDto.setEnd(LocalDateTime.now().plusDays(2L));
    }
}
