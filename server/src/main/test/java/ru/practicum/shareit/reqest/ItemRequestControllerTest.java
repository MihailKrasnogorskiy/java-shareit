package java.ru.practicum.shareit.reqest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.requests.ItemRequestController;
import ru.practicum.shareit.requests.dto.CreatedItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * тестовый класс контроллера запросов
 */
@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    private final LocalDateTime created = LocalDateTime.now().withNano(0);
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemRequestService service;
    @Autowired
    private MockMvc mvc;
    private ItemRequestDto dto;

    /**
     * создание окружения
     */
    @BeforeEach
    void createEnvironment() {
        dto = ItemRequestDto.builder()
                .id(1)
                .created(created)
                .description("test description")
                .requester(2L)
                .items(new ArrayList<>())
                .build();
    }

    /**
     * создание запроса
     *
     * @throws Exception
     */
    @Test
    void test28_create() throws Exception {
        CreatedItemRequestDto createdDto = new CreatedItemRequestDto();
        createdDto.setDescription("test description");

        Mockito
                .when(service.create(anyLong(), any()))
                .thenReturn(dto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(createdDto))
                        .header("X-Sharer-User-Id", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(dto.getDescription()), String.class))
                .andExpect(jsonPath("$.requester", is(dto.getRequester()), Long.class))
                .andExpect(jsonPath("$.created", is(dto.getCreated().toString()), String.class));
    }

    /**
     * поиск по id
     *
     * @throws Exception
     */
    @Test
    void test29_getById() throws Exception {
        Mockito
                .when(service.getById(anyLong(), anyLong()))
                .thenReturn(dto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(dto.getDescription()), String.class))
                .andExpect(jsonPath("$.requester", is(dto.getRequester()), Long.class))
                .andExpect(jsonPath("$.created", is(dto.getCreated().toString()), String.class));
    }

    /**
     * поиск всех запросов пользователя
     *
     * @throws Exception
     */
    @Test
    void test30_findAllByUser() throws Exception {
        Mockito
                .when(service.findAllByUser(anyLong()))
                .thenReturn(List.of(dto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(dto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].requester", is(dto.getRequester()), Long.class))
                .andExpect(jsonPath("$[0].created", is(dto.getCreated().toString()), String.class));
    }

    /**
     * поиск всех хапросов других пользователей
     *
     * @throws Exception
     */
    @Test
    void test31_findAllOnPage() throws Exception {
        Mockito
                .when(service.findAllOnPage(anyLong(), any(), any()))
                .thenReturn(List.of(dto));

        mvc.perform(get("/requests/all?from=0&size=20")
                        .header("X-Sharer-User-Id", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(dto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(dto.getDescription()), String.class))
                .andExpect(jsonPath("$[0].requester", is(dto.getRequester()), Long.class))
                .andExpect(jsonPath("$[0].created", is(dto.getCreated().toString()), String.class));
    }
}
