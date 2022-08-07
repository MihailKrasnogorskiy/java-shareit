package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreatingBookingDTO;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EndDateValidateException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public BookingServiceImpl(BookingRepository repository, UserService userService, ItemService itemService) {
        this.repository = repository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public BookingDto create(long userId, @Valid CreatingBookingDTO bookingDto) {
        validationEndDate(bookingDto);
        userService.validateUserId(userId);
        itemService.getById(bookingDto.getItem().getId());

        return null;
    }

    private void validationEndDate(BookingDto bookingDto){
        if(bookingDto.getStart().isAfter(bookingDto.getEnd())){
            throw new EndDateValidateException();
        }
    }
}
