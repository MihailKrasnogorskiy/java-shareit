package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void findAllByUser() throws Exception {
        this.mockMvc.perform(get("/bookings?from=-1").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings?size=0").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings?state=asdasf").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings?state=all").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setStart(LocalDateTime.now().plusDays(2));
        dto.setItemId(0);
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        dto.setItemId(1);
        dto.setStart(LocalDateTime.now().minusDays(1));
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        dto.setEnd(LocalDateTime.now().minusDays(1));
        dto.setStart(LocalDateTime.now().plusDays(1));
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById() throws Exception {
        this.mockMvc.perform(get("/bookings/1").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings/0").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAllByOwner() throws Exception {
        this.mockMvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    void approve() throws Exception {
        this.mockMvc.perform(get("/bookings/1?approved=true").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings/0?approved=true").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
    }
}