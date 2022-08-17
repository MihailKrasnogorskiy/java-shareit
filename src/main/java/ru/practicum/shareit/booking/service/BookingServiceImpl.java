package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.CreatingBookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    public BookingDto create(long userId, CreatingBookingDTO bookingDto) {
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
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            repository.save(booking);
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
        List<Booking> list = repository.findByBooker_id(userId);
        if (list.isEmpty()) {
            new NotFoundException("This user hasn't bookings");
        }
        if (state.equals(BookingState.ALL)) {
            return repository.findByBooker_id(userId).stream()
                    .map(mapper::toBookingDto)
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        } else if (state.equals(BookingState.REJECTED)) {
            return repository.findByBooker_idAndStatus(userId, BookingStatus.REJECTED).stream()
                    .map(mapper::toBookingDto)
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        } else if (state.equals(BookingState.WAITING)) {
            return repository.findByBooker_idAndStatus(userId, BookingStatus.WAITING).stream()
                    .map(mapper::toBookingDto)
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        } else {
            return repository.findByBooker_id(userId).stream()
                    .map(mapper::toBookingDto)
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<BookingDto> findAllByOwner(long ownerId, BookingState state) {
        userService.validateUserId(ownerId);
        List<Booking> list = repository.findByOwnerId(ownerId);
        if (list.isEmpty()) {
            new NotFoundException("This user hasn't bookings");
        }
        if (state.equals(BookingState.ALL)) {
            return repository.findByOwnerId(ownerId).stream()
                    .map(mapper::toBookingDto)
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        } else if (state.equals(BookingState.REJECTED)) {
            System.out.println(BookingStatus.REJECTED);
            return repository.findByOwnerIdAndStatus(ownerId, "REJECTED").stream()
                    .map(mapper::toBookingDto)
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        } else if (state.equals(BookingState.WAITING)) {
            return repository.findByOwnerIdAndStatus(ownerId, "WAITING").stream()
                    .map(mapper::toBookingDto)
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        } else {
            return repository.findByOwnerId(ownerId).stream()
                    .map(mapper::toBookingDto)
                    .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<BookingDto> findAllByItemId(long itemId) {
        return repository.findByItem_id(itemId).stream()
                .map(mapper::toBookingDto)
                .sorted((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                .collect(Collectors.toList());
    }

    private void validationEndDate(CreatingBookingDTO bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new EndDateValidateException();
        }
    }
}
