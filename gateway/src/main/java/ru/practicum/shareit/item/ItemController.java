package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findAllByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get items with userId={}, from={}, size={}", userId, from, size);
        return itemClient.findAllByUser(userId, from, size);
    }
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long itemId) {
        log.info("Get item {}, userId={}", itemId, userId);
        return itemClient.getById(userId, itemId);
    }
}
