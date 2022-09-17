package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreatingBookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * тест класс контроллера бронирований
 */
@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mvc;

    BookingDto bookingDto;
    LocalDateTime start = LocalDateTime.now().withNano(0).plusDays(1);
    LocalDateTime end = start.plusDays(1);

    /**
     * сожание окружения
     */
    @BeforeEach
    void createEnvironment() {
        bookingDto = BookingDto.builder()
                .id(1L)
                .status(BookingStatus.WAITING)
                .start(start)
                .end(end)
                .booker(new UserDto(2L, "test", "test@test.ru"))
                .item(new ItemDto(1L, "something", "description", true, 1, null,
                        null, null, new HashSet<>()))
                .build();
    }

    /**
     * тест создания бронирования
     *
     * @throws Exception
     */
    @Test
    void test16_createNewBooking() throws Exception {
        CreatingBookingDto creatingDto = CreatingBookingDto.builder()
                .booker(bookingDto.getBooker().getId())
                .itemId(bookingDto.getItem().getId())
                .start(start)
                .end(end)
                .build();

        Mockito
                .when(bookingService.create(anyLong(), any()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(creatingDto))
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName()), String.class))
                .andExpect(jsonPath("$.status", is("WAITING")))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription()), String.class));
    }

    /**
     * тест подтверждения/отклонения бронирования
     *
     * @throws Exception
     */
    @Test
    void test17_approve() throws Exception {
        bookingDto.setStatus(BookingStatus.APPROVED);

        Mockito
                .when(bookingService.approve(anyLong(), any(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName()), String.class))
                .andExpect(jsonPath("$.status", is("APPROVED")))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription()), String.class));
    }

    /**
     * тест поиска по id
     *
     * @throws Exception
     */
    @Test
    void test18_findById() throws Exception {

        Mockito
                .when(bookingService.findById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName()), String.class))
                .andExpect(jsonPath("$.status", is("WAITING")))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription()), String.class));
    }

    /**
     * тест поиска всех бронирований пользователя
     *
     * @throws Exception
     */
    @Test
    void test19_findAllByUser() throws Exception {

        Mockito
                .when(bookingService.findAllByUser(anyLong(), any(), any(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings?from=2&size=2")
                        .header("X-Sharer-User-Id", 2)
                        .queryParam("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDto.getBooker().getName()), String.class))
                .andExpect(jsonPath("$[0].status", is("WAITING")))
                .andExpect(jsonPath("$[0].item.description", is(bookingDto.getItem().getDescription()), String.class));
    }

    /**
     * тест поиска всех бронирований бладельца вещи
     *
     * @throws Exception
     */
    @Test
    void test20_findAllByOwner() throws Exception {

        Mockito
                .when(bookingService.findAllByOwner(anyLong(), any(), any(), any()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner?from=2&size=2")
                        .header("X-Sharer-User-Id", 1)
                        .queryParam("state", "ALL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDto.getBooker().getName()), String.class))
                .andExpect(jsonPath("$[0].status", is("WAITING")))
                .andExpect(jsonPath("$[0].item.description", is(bookingDto.getItem().getDescription()), String.class));
    }
}