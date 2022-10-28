package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    void findAllByUser() throws Exception {
        this.mockMvc.perform(get("/bookings?from=-1"))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings?size=0"))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/bookings?state=asdasf"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAllByOwner() {
    }

    @Test
    void approve() {
    }
}