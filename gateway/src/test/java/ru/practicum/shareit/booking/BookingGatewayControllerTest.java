package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * тестовый класс контроллера бронирований
 */
@SpringBootTest
@AutoConfigureMockMvc
class BookingGatewayControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingClient client;
    @Autowired
    ObjectMapper mapper;

    /**
     * запрос всех бронирований пользователя
     */
    @Test
    void test01_findAllByUser() throws Exception {
        Mockito
                .when(client.findAllByUser(Mockito.anyLong(), Mockito.any(BookingState.class), Mockito.anyInt(),
                        Mockito.anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(get("/bookings?from=-1").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings?size=0").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings?state=asdasf").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings?state=all").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    /**
     * создание бронирования
     */
    @Test
    void test02_create() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        dto.setItemId(1);
        Mockito
                .when(client.create(Mockito.anyLong(), Mockito.any(BookingDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(post("/bookings").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        dto.setItemId(0);
        this.mockMvc.perform(post("/bookings").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        dto.setItemId(1);
        dto.setStart(LocalDateTime.now().minusDays(1));
        this.mockMvc.perform(post("/bookings").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        dto.setEnd(LocalDateTime.now().minusDays(1));
        dto.setStart(LocalDateTime.now().plusDays(1));
        this.mockMvc.perform(post("/bookings").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * запрос бронирования по id
     */
    @Test
    void test03_findById() throws Exception {
        Mockito
                .when(client.findById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(get("/bookings/1").header("X-Sharer-User-Id", 10))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/bookings/1").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings/0").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
    }

    /**
     * запрос всех бронирований на вещи пользователя
     */
    @Test
    void test04_findAllByOwner() throws Exception {
        Mockito
                .when(client.findAllByOwner(Mockito.anyLong(), Mockito.any(BookingState.class), Mockito.anyInt(),
                        Mockito.anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", 10))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
    }

    /**
     * подтверждение бронирования
     */
    @Test
    void test05_approve() throws Exception {
        Mockito
                .when(client.approve(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(patch("/bookings/1?approved=true").header("X-Sharer-User-Id", 10))
                .andExpect(status().isOk());
        this.mockMvc.perform(patch("/bookings/1?approved=true").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(patch("/bookings/0?approved=true").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
    }
}