package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemUpdate;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class ItemControllerTest {
    private UserDto user = UserDto.builder()
            .name("Voldemar")
            .email("voldemar@mail.ru")
            .build();
    private ItemDto itemDto = ItemDto.builder()
            .name("Машина")
            .description("Audi TT")
            .available(true)
            .build();
    private ItemDto itemDto1 = ItemDto.builder()
            .id(1)
            .name("Машина")
            .description("Audi TT")
            .available(true)
            .owner(1)
            .requestId(0)
            .build();
    @Autowired
    private ItemRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    /**
     * возвращение всех вещей пользователя по id пользователя
     */
    @Test
    void test06_getAllByUserId() throws Exception {
        repository.clear();
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<ItemDto> list = new ArrayList<>();
        list.add(itemDto1);
        this.mockMvc.perform(get("/items").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(list)));
        itemDto.setName("Прицеп");
        itemDto.setDescription("Лодочный прицеп МЗСА G");
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Прицеп")
                .description("Лодочный прицеп МЗСА G")
                .available(true)
                .owner(1)
                .requestId(0)
                .build();
        list.add(itemDto2);
        this.mockMvc.perform(get("/items").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(list)));
        this.mockMvc.perform(get("/items").header("X-Sharer-User-Id", 2))
                .andExpect(status().isNotFound());
    }

    /**
     * возвращение вещи по id
     */
    @Test
    void test07_getById() throws Exception {
        repository.clear();
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/items/1")).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
    }

    /**
     * обновление вещи
     */
    @Test
    void test08_update() throws Exception {
        repository.clear();
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
        ItemUpdate itemUpdate = ItemUpdate.builder()
                .name("Лодочный мотор")
                .description("Honda 10")
                .available(true)
                .build();
        itemDto1.setName("Лодочный мотор");
        itemDto1.setDescription("Honda 10");
        this.mockMvc.perform(patch("/items/1").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
        itemUpdate.setAvailable(false);
        itemDto1.setAvailable(false);
        this.mockMvc.perform(patch("/items/1").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
        this.mockMvc.perform(patch("/items/1").header("X-Sharer-User-Id", 2)
                        .content(mapper.writeValueAsString(itemUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        itemUpdate.setName("");
        this.mockMvc.perform(patch("/items/1").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        itemUpdate.setDescription("");
        this.mockMvc.perform(patch("/items/1").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * создание вещи
     */
    @Test
    void test09_create() throws Exception {
        repository.clear();
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
        itemDto.setName(null);
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        itemDto.setName("");
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        itemDto.setName("Машина");
        itemDto.setAvailable(null);
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        itemDto.setDescription("");
        itemDto.setAvailable(false);
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        itemDto.setDescription(null);
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * поиск вещей без учёта регистра в полях имя и описание
     */
    @Test
    void test10_search() throws Exception {
        repository.clear();
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
        List<ItemDto> list = new ArrayList<>();
        list.add(itemDto1);
        itemDto.setName("Audi");
        itemDto.setDescription("TT");
        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Audi")
                .description("TT")
                .available(true)
                .owner(1)
                .requestId(0)
                .build();
        list.add(itemDto2);
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto2)));
        this.mockMvc.perform(get("/items/search?text=aUdI")).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(list)));
        list.remove(1);
        ItemUpdate itemUpdate = ItemUpdate.builder().available(false).build();
        this.mockMvc.perform(patch("/items/2").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/items/search?text=aUdI")).andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(list)));
    }

    /**
     * удаление пользователя
     */
    @Test
    void test11_delete() throws Exception {
        repository.clear();
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
        this.mockMvc.perform(delete("/items/1").header("X-Sharer-User-Id", 2))
                .andExpect(status().isNotFound());
        this.mockMvc.perform(delete("/items/1").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/items/1")).andExpect(status().isNotFound());
    }

    /**
     * создание окружения для тестов
     */
    @BeforeAll
    void createEnvironment() throws Exception {
        userRepository.clear();
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        user.setEmail("mail@mail.ru");
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    /**
     * возвращение объектов в исходное состояние
     */
    @BeforeEach
    void restoreItemsDto() {
        itemDto.setName("Машина");
        itemDto.setDescription("Audi TT");
        itemDto.setAvailable(true);
        itemDto1.setName("Машина");
        itemDto1.setDescription("Audi TT");
        itemDto1.setAvailable(true);
    }
}