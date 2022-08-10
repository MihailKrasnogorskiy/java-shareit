package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.CreatingBookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EndDateValidateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper mapper;

    @Autowired
    public BookingServiceImpl(BookingRepository repository, UserService userService, ItemService itemService, BookingMapper mapper) {
        this.repository = repository;
        this.userService = userService;
        this.itemService = itemService;
        this.mapper = mapper;
    }

    @Override
    public BookingDto create(long userId, @Valid CreatingBookingDTO bookingDto) {
        validationEndDate(bookingDto);
        userService.validateUserId(userId);
        bookingDto.setBooker(userId);
       if(!itemService.getById(bookingDto.getItemId()).getAvailable()){
           throw new NotFoundException("This Item has not booking");
       }
        Booking booking = mapper.toBooking(bookingDto);
        BookingDto returnedDto = mapper.toBookingDto(repository.save(repository.save(booking)));
        log.info("Booking with id {} has been created", returnedDto.getId());
        return returnedDto;
    }

    private void validationEndDate(CreatingBookingDTO bookingDto){
        if(bookingDto.getStart().isAfter(bookingDto.getEnd())){
            throw new EndDateValidateException();
        }
    }
}
