package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreatingBookingDTO;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                             @Valid @RequestBody CreatingBookingDTO bookingDto) {
        return service.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam Boolean approved,
                              @PathVariable long bookingId) {
        return service.approve(userId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        return service.findById(userId, bookingId);
    }
}
