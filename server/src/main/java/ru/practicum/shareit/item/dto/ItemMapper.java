package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.HashSet;

/**
 * маппер вещей
 */
@Component
public class ItemMapper {
    private final UserRepository userRepository;
    private final ItemRequestRepository requestRepository;

    @Autowired
    public ItemMapper(UserRepository userRepository, ItemRequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    /**
     * создание dto объекта вещи
     *
     * @param item объект вещи
     * @return dto объект вещи
     */
    public ItemDto toItemDto(Item item) {
        ItemDto dto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner().getId())
                .lastBooking(null)
                .nextBooking(null)
                .comments(new HashSet<>())
                .build();
        if (item.getRequest() != null) {
            dto.setRequestId(item.getRequest().getId());
        }
        return dto;
    }

    /**
     * создание объекта вещи
     *
     * @param itemDto dto объект вещи
     * @return объект вещи
     */
    public Item toItem(ItemDto itemDto) {
        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
        item.setOwner(userRepository.findById(itemDto.getOwner()).get());
        if (itemDto.getRequestId() != null) {
            item.setRequest(requestRepository.findById(itemDto.getRequestId()).get());
        }
        return item;
    }

    /**
     * соджания дто объекта вещи для добавления в запрос
     *
     * @param item - объект вещи
     * @return дто объект вещи для добавления в запрос
     */
    public ItemDtoForRequest itemDtoForRequest(Item item) {
        return ItemDtoForRequest.builder()
                .id(item.getId())
                .userId(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest().getId())
                .build();
    }
}
