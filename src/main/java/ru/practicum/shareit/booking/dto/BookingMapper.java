package ru.practicum.shareit.booking.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
public class BookingMapper {

    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Autowired
    public BookingMapper(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public Booking toBooking(CreatingBookingDTO bookingDTO) {
        return Booking.builder()
                .start(bookingDTO.getStart())
                .end(bookingDTO.getEnd())
                .item(itemRepository.findById(bookingDTO.getItem()).get())
                .booker(userRepository.findById(bookingDTO.getBooker()).get())
                .build();
    }
}
