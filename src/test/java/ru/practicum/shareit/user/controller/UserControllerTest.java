package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    private User user = User.builder()
            .name("Voldemar")
            .email("voldemar@mail.ru")
            .build();
    @Autowired
    private UserController controller;
    @Autowired
    private UserRepository repository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;


    @Test
    void test01_getAllUsers() throws Exception {
        repository.clear();
        assertTrue(controller.getAllUsers().isEmpty());
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertFalse(controller.getAllUsers().isEmpty());
        assertEquals(1, controller.getAllUsers().size());
        assertEquals("Voldemar", controller.getAllUsers().get(0).getName());
        user.setEmail("mail@mail.com");
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(2, controller.getAllUsers().size());
        assertEquals("Voldemar", controller.getAllUsers().get(0).getName());
        assertEquals("mail@mail.com", controller.getAllUsers().get(1).getEmail());
    }

    @Test
    void test02_getById() throws Exception {
        repository.clear();
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        user.setId(1);
        this.mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(user)));

        this.mockMvc.perform(get("/users/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void test03_createUser() throws Exception {
        repository.clear();
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(1, controller.getAllUsers().size());
        assertEquals("Voldemar", controller.getAllUsers().get(0).getName());
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        user.setEmail("vova.ru");
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        user.setEmail("mail@mail.ru");
        user.setName("");
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test04_updateUser() throws Exception {
        repository.clear();
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        user.setName("Vasya");
        user.setEmail(null);
        this.mockMvc.perform(patch("/users/1").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertFalse(controller.getAllUsers().isEmpty());
        assertEquals(1, controller.getAllUsers().size());
        assertEquals(1, controller.getById(1).getId());
        assertEquals("Vasya", controller.getById(1).getName());
        assertEquals("voldemar@mail.ru", controller.getById(1).getEmail());
        user.setName(null);
        user.setEmail("vasya@gmail.com");
        this.mockMvc.perform(patch("/users/1").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertFalse(controller.getAllUsers().isEmpty());
        assertEquals(1, controller.getAllUsers().size());
        assertEquals(1, controller.getById(1).getId());
        assertEquals("Vasya", controller.getById(1).getName());
        assertEquals("vasya@gmail.com", controller.getById(1).getEmail());
        user.setName(null);
        user.setEmail("vasya.com");
        this.mockMvc.perform(patch("/users/1").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        user.setName(null);
        user.setEmail("vasya@gmail.com");
        repository.getAllUsersEmail().stream()
                .forEach(System.out::println);
        this.mockMvc.perform(patch("/users/1").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        user.setName("");
        user.setEmail(null);
        this.mockMvc.perform(patch("/users/1").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test05_deleteUser() throws Exception {
        repository.clear();
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(1, controller.getById(1).getId());
        assertEquals("Voldemar", controller.getById(1).getName());
        assertEquals("voldemar@mail.ru", controller.getById(1).getEmail());
        this.mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        assertTrue(controller.getAllUsers().isEmpty());
    }
}