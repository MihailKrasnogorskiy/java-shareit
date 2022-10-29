package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * тестовый класс контроллера пользователей
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserClient client;
    @Autowired
    ObjectMapper mapper;

    @Test
    void test15_create() throws Exception {
        UserDto dto = UserDto.builder().name("name").email("mail@mail.ru").build();
        Mockito.when(client.create(Mockito.any(UserDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        dto.setName("");
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
        dto.setName("name");
        dto.setEmail("mail");
        this.mockMvc.perform(post("/users").content(mapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void test16_update() throws Exception {
        UserUpdateDto dto = new UserUpdateDto();
        Mockito.when(client.update(Mockito.anyLong(), Mockito.any(UserUpdateDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(patch("/users/1").content(mapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        dto.setEmail("mail");
        this.mockMvc.perform(patch("/users/1").content(mapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
        dto.setEmail("mail@mail.ru");
        this.mockMvc.perform(patch("/users/0").content(mapper.writeValueAsString(dto)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void test17_getById() throws Exception {
        Mockito.when(client.getById(Mockito.anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(get("/users/1")).andExpect(status().isOk());
        this.mockMvc.perform(get("/users/0")).andExpect(status().isBadRequest());
    }

    @Test
    void test18_test_delete() throws Exception {
        Mockito.when(client.deleteById(Mockito.anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(delete("/users/1")).andExpect(status().isOk());
        this.mockMvc.perform(delete("/users/0")).andExpect(status().isBadRequest());
    }

    @Test
    void test19_getAll() throws Exception {
        Mockito.when(client.getAll()).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        this.mockMvc.perform(get("/users")).andExpect(status().isOk());
    }
}