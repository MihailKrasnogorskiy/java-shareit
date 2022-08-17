package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingDto;

/**
 * класс Dto сдаваемой в аренду вещи  данными о бронированиях
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ItemDtoWithBooking extends ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long owner;
    private long requestId;
    private BookingDto lastBooking;
    private BookingDto nextBooking;

    public ItemDtoWithBooking(long id, String name, String description, Boolean available, long owner, long requestId) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.requestId = requestId;
        this.lastBooking = null;
        this.nextBooking = null;
    }
}
