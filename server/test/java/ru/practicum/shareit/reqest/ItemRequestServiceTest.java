package ru.practicum.shareit.reqest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.dto.CreatedItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * тестовый класс сервиса запросов
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemRequestServiceTest {

    @Autowired
    ItemRequestService service;
    CreatedItemRequestDto createdDto = new CreatedItemRequestDto();
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    /**
     * создание запроса
     */
    @Test
    void test32_create() {
        createdDto.setDescription("test");
        ItemRequestDto dto = service.create(2L, createdDto);
        assertEquals(1, dto.getId());
        assertNotNull(dto.getCreated());
        assertEquals("test", dto.getDescription());
        assertEquals(0, dto.getItems().size());
        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            service.create(99L, createdDto);
        });
        assertNotNull(thrown.getMessage());
    }

    /**
     * поиск по id
     */
    @Test
    void test33_getById() {
        createdDto.setDescription("test");
        service.create(2L, createdDto);
        ItemDto itemDto = ItemDto.builder()
                .name("Машина")
                .description("Audi TT")
                .available(true)
                .owner(1)
                .requestId(1L)
                .comments(new HashSet<>())
                .build();
        itemService.create(1, itemDto);
        ItemRequestDto dto = service.getById(1L, 2L);
        assertEquals(1, dto.getId());
        assertNotNull(dto.getCreated());
        assertEquals("test", dto.getDescription());
        assertEquals(1, dto.getItems().size());
        assertEquals(2, dto.getItems().get(0).getId());
        Throwable thrown = assertThrows(NotFoundException.class, () -> {
            service.getById(22L, 2L);
        });
        assertEquals("This request not found", thrown.getMessage());
    }

    /**
     * поиск всех запросов пользователя
     */
    @Test
    void test34_findAllByUser() {
        createdDto.setDescription("test");
        service.create(2L, createdDto);
        createdDto.setDescription("test2");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.create(2L, createdDto);
        ItemDto itemDto = ItemDto.builder()
                .name("Машина")
                .description("Audi TT")
                .available(true)
                .owner(1)
                .requestId(1L)
                .comments(new HashSet<>())
                .build();
        itemService.create(1, itemDto);
        List<ItemRequestDto> list = service.findAllByUser(2L);
        assertEquals(2, list.size());
        assertEquals(1, list.get(1).getId());
        assertNotNull(list.get(0).getCreated());
        assertEquals("test", list.get(1).getDescription());
        assertEquals(1, list.get(1).getItems().size());
        assertEquals(2, list.get(1).getItems().get(0).getId());
        assertEquals(2, list.get(0).getId());
        assertNotNull(list.get(0).getCreated());
        assertEquals("test2", list.get(0).getDescription());
        assertEquals(0, list.get(0).getItems().size());
    }

    /**
     * поиск всех запросов других пользователей
     */
    @Test
    void test35_findAllOnPage() {
        createdDto.setDescription("test");
        service.create(1L, createdDto);
        createdDto.setDescription("test2");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.create(1L, createdDto);
        ItemDto itemDto = ItemDto.builder()
                .name("Машина")
                .description("Audi TT")
                .available(true)
                .owner(2)
                .requestId(1L)
                .comments(new HashSet<>())
                .build();
        itemService.create(1, itemDto);
        List<ItemRequestDto> list = service.findAllOnPage(2L, 0, 2);
        assertEquals(2, list.size());
        assertEquals(1, list.get(1).getId());
        assertNotNull(list.get(0).getCreated());
        assertEquals("test", list.get(1).getDescription());
        assertEquals(1, list.get(1).getItems().size());
        assertEquals(2, list.get(1).getItems().get(0).getId());
        assertEquals(2, list.get(0).getId());
        assertNotNull(list.get(0).getCreated());
        assertEquals("test2", list.get(0).getDescription());
        assertEquals(0, list.get(0).getItems().size());
    }

    /**
     * создание окружения
     */
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
        itemService.create(1, itemDto);
    }

    /**
     * очистка окружения
     */
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
        query = "TRUNCATE TABLE requests ";
        jdbcTemplate.update(query);
        query = "ALTER TABLE requests ALTER COLUMN id RESTART WITH 1";
        jdbcTemplate.update(query);
        query = "SET REFERENTIAL_INTEGRITY = TRUE";
        jdbcTemplate.update(query);
    }
}
