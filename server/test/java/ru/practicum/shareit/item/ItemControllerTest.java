package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.CreatingBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.CommentatorValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.ItemUpdate;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class ItemControllerTest {
    private final UserDto user = UserDto.builder()
            .name("Voldemar")
            .email("voldemar@mail.ru")
            .build();
    private final ItemDto itemDto = ItemDto.builder()
            .name("Машина")
            .description("Audi TT")
            .available(true)
            .owner(1)
            .comments(new HashSet<>())
            .build();
    private final ItemDto itemDto1 = ItemDto.builder()
            .id(1)
            .name("Машина")
            .description("Audi TT")
            .available(true)
            .owner(1)
            .requestId(null)
            .comments(new HashSet<>())
            .build();
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemService service;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * возвращение всех вещей пользователя по id пользователя
     */
    @Test
    @Transactional
    void test06_getAllByUserId() throws Exception {
        clear();
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
                .requestId(null)
                .comments(new HashSet<>())
                .build();
        list.add(itemDto2);
        this.mockMvc.perform(get("/items").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(list)));
        list.clear();
        this.mockMvc.perform(get("/items").header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(list)));
    }

    /**
     * возвращение вещи по id
     */
    @Test
    @Transactional
    void test07_getById() throws Exception {
        clear();
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/items/1").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
    }

    /**
     * обновление вещи
     */
    @Test
    @Transactional
    void test08_update() throws Exception {
        clear();
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
    @Transactional
    void test09_create() throws Exception {
        clear();
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
    @Transactional
    void test10_search() throws Exception {
        clear();
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
                .requestId(null)
                .comments(new HashSet<>())
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
    @Transactional
    void test11_delete() throws Exception {
        clear();
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
        this.mockMvc.perform(delete("/items/1").header("X-Sharer-User-Id", 2))
                .andExpect(status().isNotFound());
        this.mockMvc.perform(delete("/items/1").header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/items/1").header("X-Sharer-User-Id", 1))
                .andExpect(status().isNotFound());
    }

    /**
     * добавление комментария
     */
    @Test
    @Transactional
    void test37_addComment() throws Exception {
        clear();
        this.mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)));
        LocalDateTime start = LocalDateTime.now().withSecond(0).withNano(0).minusDays(2);
        LocalDateTime end = start.plusDays(1);

        CreatingBookingDto creatingDto = CreatingBookingDto.builder()
                .booker(2L)
                .itemId(1L)
                .start(start)
                .end(end)
                .build();
        bookingService.create(2L, creatingDto);
        String commentDto = "{\"text\": \"Comment for item 1\"}";
        this.mockMvc.perform(post("/items/1/comment").header("X-Sharer-User-Id", 2)
                        .content(commentDto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<Comment> list = commentRepository.findByItemId(1L);
        assertEquals(1, list.get(0).getId());
        assertEquals(1, list.get(0).getItem().getId());
        assertEquals("Comment for item 1", list.get(0).getText());
        Throwable thrown = assertThrows(CommentatorValidationException.class, () -> {
            service.addComment(1L, 1L, CommentDto.builder().text("text").build());
        });
        assertEquals("You are not booker", thrown.getMessage());
    }

    /**
     * создание окружения для тестов
     */
    @BeforeAll
    void createEnvironment() throws Exception {
        clear();
        String query = "ALTER TABLE users ALTER COLUMN id RESTART WITH 1";
        jdbcTemplate.update(query);
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        user.setEmail("mail@mail.ru");
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @AfterAll
    void clearEnvironment() {
        clear();
        String query = "SET REFERENTIAL_INTEGRITY = FALSE";
        jdbcTemplate.update(query);
        query = "TRUNCATE TABLE users ";
        jdbcTemplate.update(query);
        query = "ALTER TABLE users ALTER COLUMN id RESTART WITH 1";
        jdbcTemplate.update(query);
        query = "SET REFERENTIAL_INTEGRITY = TRUE";
        jdbcTemplate.update(query);
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

    /**
     * сброс автоинкремента в таблице items
     */
    private void clear() {
        String query = "ALTER TABLE items ALTER COLUMN id RESTART WITH 1";
        jdbcTemplate.update(query);
    }
}