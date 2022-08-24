package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.CreatedItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @Autowired
    public ItemRequestController(ItemRequestService service) {
        this.service = service;
    }


    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @Valid @RequestBody CreatedItemRequestDto dto) {
        return service.create(userId, dto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@PathVariable long requestId) {
        return service.getById(requestId);
    }

    @GetMapping
    public List<ItemRequestDto> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.findAll(userId);
    }
}
