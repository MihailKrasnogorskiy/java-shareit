package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.OffsetLimitPageable;
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
    @Transactional
    public List<BookingDto> findAllByUser(long userId, BookingState state, Integer from, Integer size) {

        userService.validateUserId(userId);
        Pageable pageable = OffsetLimitPageable.of(from, size, Sort.unsorted());
        LocalDateTime now = LocalDateTime.now();
        List<BookingDto> result = new ArrayList<>();
        switch (state) {
            case ALL:
                result = repository.findAllByBookerIdOrderByStartDesc(userId, pageable).stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case PAST:
                result = repository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, now, pageable).stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case WAITING:
                result = repository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING,
                                pageable)
                        .stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                result = repository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED,
                                pageable)
                        .stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                result = repository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, now, pageable).stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                result = repository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now,
                                now, pageable).stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
        }
        return result;
    }

    @Override
    @Transactional
    public List<BookingDto> findAllByOwner(long ownerId, BookingState state, Integer from, Integer size) {
        userService.validateUserId(ownerId);
        Pageable pageable = OffsetLimitPageable.of(from, size, Sort.unsorted());
        LocalDateTime now = LocalDateTime.now();
        List<BookingDto> result = new ArrayList<>();
        switch (state) {
            case ALL:
                result = repository.findAllByItemOwnerIdOrderByStartDesc(ownerId, pageable).stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case PAST:
                result = repository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, now, pageable).stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case WAITING:
                result = repository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING,
                                pageable)
                        .stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                result = repository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED,
                                pageable)
                        .stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                result = repository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, now, pageable).stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                result = repository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, now,
                                now, pageable).stream()
                        .map(mapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
        }
        return result;
    }

    @Override
    public List<BookingDto> findAllByItemId(long itemId) {
        return repository.findByItem_id(itemId).stream()
                .map(mapper::toBookingDto)
                .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getPastByUser(Long userId) {
        userService.validateUserId(userId);
        return repository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()).stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
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
