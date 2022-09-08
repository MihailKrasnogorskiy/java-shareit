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
    LocalDateTime start = LocalDateTime.now().withNano(0).plusDays(1);
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
        itemService.update(1L, 1L,itemDto);
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
    void test24_findById(){
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

    private void createBookings(){

    }
}
