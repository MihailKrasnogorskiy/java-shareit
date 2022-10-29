package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * тестовый класс запросов на бронирование вещей
 */
@SpringBootTest
@AutoConfigureMockMvc
class RequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RequestClient client;
    @Autowired
    ObjectMapper mapper;

    /**
     * создание  запроса бронирования вещи
     */
    @Test
    void test11_create() throws Exception {
        ItemRequestDto dto = ItemRequestDto.builder()
                .description("des")
                .created(LocalDateTime.now().plusDays(1))
                .build();
        Mockito
                .when(client.create(Mockito.anyLong(), Mockito.any(ItemRequestDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(post("/requests").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        dto.setDescription("");
        this.mockMvc.perform(post("/requests").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        dto.setDescription("des");
        dto.setCreated(LocalDateTime.now().minusDays(1));
        this.mockMvc.perform(post("/requests").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        dto.setCreated(LocalDateTime.now().plusDays(1));
        this.mockMvc.perform(post("/requests").header("X-Sharer-User-Id", 0)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * поиск всех запросов пользователя
     */
    @Test
    void test12_findAllByUser() throws Exception {
        Mockito
                .when(client.findAllByUser(Mockito.anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(get("/requests").header("X-Sharer-User-Id", 10))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/requests").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
    }

    /**
     * поиск все запросов других пользователей
     */
    @Test
    void test13_findAllOnPage() throws Exception {
        Mockito
                .when(client.findAllOnPage(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(get("/requests/all").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/requests/all").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/requests/all?from=-1&size=4").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/requests/all?from=0&size=0").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
    }

    /**
     * поиск запроса бронирования по id
     */
    @Test
    void test14_getById() throws Exception {
        Mockito
                .when(client.getById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(get("/requests/1").header("X-Sharer-User-Id", 10))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/requests/1").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/requests/0").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
    }
}