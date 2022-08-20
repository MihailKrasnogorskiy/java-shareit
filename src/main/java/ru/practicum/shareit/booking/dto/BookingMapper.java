package ru.practicum.shareit.booking.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

/**
 * маппер бронирований
 */
@Component
public class BookingMapper {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public BookingMapper(ItemRepository itemRepository, UserRepository userRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemMapper = itemMapper;
    }

    /**
     * создание объекта бронирования из dto объкта создания бронирования
     *
     * @param bookingDto dto объект создания бронирования
     * @return объект бронирования
     */
    public Booking toBooking(CreatingBookingDto bookingDto) {
        return Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(itemRepository.findById(bookingDto.getItemId()).get())
                .booker(userRepository.findById(bookingDto.getBooker()).get())
                .status(BookingStatus.WAITING)
                .build();
    }

    /**
     * создание dto объекта бронирования из объкта бронирования
     *
     * @param booking объект бронирования
     * @return dto объект бронирования
     */
    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemMapper.toItemDto(booking.getItem()))
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    /**
     * создание dto объекта бронирования для включения в dto объект вещи из dto объкта бронирования
     *
     * @param bookingDto dto объект бронирования
     * @return dto объект бронирования для включения в dto объект вещи
     */
    public BookingDtoForItemDto toBookingForItemDto(BookingDto bookingDto) {
        return BookingDtoForItemDto.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .itemId(bookingDto.getItem().getId())
                .bookerId(bookingDto.getBooker().getId())
                .status(bookingDto.getStatus())
                .build();
    }
}
