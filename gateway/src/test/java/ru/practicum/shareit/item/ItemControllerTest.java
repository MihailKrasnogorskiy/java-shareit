package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * естовый класс контроллера вещей
 */
@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemClient client;
    @Autowired
    ObjectMapper mapper;

    /**
     * поиск вещей пользователя
     */
    @Test
    void test06_findAllByUser() throws Exception {
        Mockito
                .when(client.findAllByUser(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(get("/items").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/items").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/items?from=-1&size=4").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/items?from=0&size=0").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
    }

    /**
     * поиск вещи по id
     */
    @Test
    void test07_getById() throws Exception {
        Mockito
                .when(client.getById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(get("/items/1").header("X-Sharer-User-Id", 10))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/items/1").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/items/0").header("X-Sharer-User-Id", 1))
                .andExpect(status().isBadRequest());
    }

    /**
     * создание вещи
     */
    @Test
    void test08_createNewItem() throws Exception {
        ItemDto dto = ItemDto.builder()
                .name("name")
                .available(true)
                .description("des")
                .build();
        Mockito
                .when(client.create(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        dto.setName("");
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        dto.setName("name");
        dto.setAvailable(null);
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        dto.setAvailable(false);
        dto.setDescription("");
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        dto.setDescription("des");
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 0)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * обновление вещи
     */
    @Test
    void test09_updateItem() throws Exception {
        ItemUpdate dto = ItemUpdate.builder().build();
        Mockito
                .when(client.update(Mockito.anyLong(), Mockito.any(ItemUpdate.class), Mockito.anyLong()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(patch("/items/1").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(patch("/items/1").header("X-Sharer-User-Id", 0)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(patch("/items/0").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * поиск
     */
    @Test
    void test10_search() throws Exception {
        Mockito
                .when(client.search(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(get("/items/search").header("X-Sharer-User-Id", 10))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/items/search").header("X-Sharer-User-Id", 0))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/items/search?from=-1&size=2").header("X-Sharer-User-Id", 10))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/items/search?from=1&size=0").header("X-Sharer-User-Id", 10))
                .andExpect(status().isBadRequest());
    }

    /**
     * добавление комментария
     */
    @Test
    void test20_addComment() throws Exception {
        CommentDto dto = CommentDto.builder()
                .text("t")
                .build();
        Mockito
                .when(client.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentDto.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(post("/items/1/comment").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        dto.setText("");
        this.mockMvc.perform(post("/items/1/comment").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        dto.setText("text");
        this.mockMvc.perform(post("/items/0/comment").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(post("/items/1/comment").header("X-Sharer-User-Id", 0)
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}