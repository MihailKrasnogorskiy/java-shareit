package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.CreatingBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * класс реализации интерфеса сервиса бронирований
 */
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper mapper;

    @Autowired
    public BookingServiceImpl(BookingRepository repository, UserService userService, ItemService itemService,
                              BookingMapper mapper) {
        this.repository = repository;
        this.userService = userService;
        this.itemService = itemService;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public BookingDto create(long userId, CreatingBookingDto bookingDto) {
        validationEndDate(bookingDto);
        userService.validateUserId(userId);
        bookingDto.setBooker(userId);
        if (itemService.getById(bookingDto.getItemId(), userId).getOwner() == userId) {
            throw new NotFoundException("You cannot book your item");
        }
        if (!itemService.getById(bookingDto.getItemId(), userId).getAvailable()) {
            throw new ItemUnavailableException();
        }
        Booking booking = mapper.toBooking(bookingDto);
        BookingDto returnedDto = mapper.toBookingDto(repository.save(repository.save(booking)));
        log.info("Booking with id {} has been created", returnedDto.getId());
        return returnedDto;
    }

    @Override
    @Transactional
    public BookingDto approve(long userId, Boolean approved, long bookingId) {
        Booking booking = repository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("This booking not found"));
        userService.validateUserId(userId);
        if (booking.getItem().getOwner().getId() != userId) {
            throw new OwnerValidationException();
        } else if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new BookingApprovedException();
        } else if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
            repository.save(booking);
            log.info("Booking with id {} has been approved", bookingId);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            repository.save(booking);
            log.info("Booking with id {} has been rejected", bookingId);
        }
        return mapper.toBookingDto(booking);
    }

    @Override
    public BookingDto findById(long userId, long bookingId) {
        userService.validateUserId(userId);
        Booking booking = repository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("This booking not found"));
        if (booking.getItem().getOwner().getId() != userId) {
            if (booking.getBooker().getId() == userId) {
                return mapper.toBookingDto(booking);
            } else {
                throw new BookerValidationException();
            }
        } else {
            return mapper.toBookingDto(booking);
        }
    }

    @Override
    public List<BookingDto> findAllByUser(long userId, BookingState state) {
        userService.validateUserId(userId);
        if (state.equals(BookingState.WAITING)) {
            return filterByState(repository.findByBooker_idAndStatus(userId, BookingStatus.WAITING), state);
        } else if (state.equals(BookingState.REJECTED)) {
            return filterByState(repository.findByBooker_idAndStatus(userId, BookingStatus.REJECTED), state);
        } else {
            return filterByState(repository.findByBooker_id(userId), state);
        }
    }

    @Override
    public List<BookingDto> findAllByOwner(long ownerId, BookingState state) {
        userService.validateUserId(ownerId);
        if (state.equals(BookingState.WAITING)) {
            return filterByState(repository.findByOwnerIdAndStatus(ownerId, "WAITING"), state);
        } else if (state.equals(BookingState.REJECTED)) {
            return filterByState(repository.findByOwnerIdAndStatus(ownerId, "REJECTED"), state);
        } else {
            return filterByState(repository.findByOwnerId(ownerId), state);
        }
    }

    @Override
    public List<BookingDto> findAllByItemId(long itemId) {
        return repository.findByItem_id(itemId).stream()
                .map(mapper::toBookingDto)
                .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                .collect(Collectors.toList());
    }

    /**
     * фильтрация списка бронирований по вариванту выборки
     *
     * @param bookings список бронирований
     * @param state    варивант выборки
     * @return список dto объектов бронирования
     */
    public List<BookingDto> filterByState(List<Booking> bookings, BookingState state) {
        if (state.equals(BookingState.ALL) || state.equals(BookingState.REJECTED) ||
                state.equals(BookingState.WAITING)) {
            return bookings.stream()
                    .map(mapper::toBookingDto)
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        } else if (state.equals(BookingState.PAST)) {
            return bookings.stream()
                    .map(mapper::toBookingDto)
                    .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        } else if (state.equals(BookingState.FUTURE)) {
            return bookings.stream()
                    .map(mapper::toBookingDto)
                    .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        } else if (state.equals(BookingState.CURRENT)) {
            return bookings.stream()
                    .map(mapper::toBookingDto)
                    .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                    .filter(b -> b.getEnd().isAfter(LocalDateTime.now()))
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * валидация даты окончания бронирования
     *
     * @param bookingDto dto объект бронирования
     */
    private void validationEndDate(CreatingBookingDto bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new EndDateValidateException();
        }
    }
}
