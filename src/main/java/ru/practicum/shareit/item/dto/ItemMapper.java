package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.HashSet;

@Component
public class ItemMapper {
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemMapper(UserRepository userRepository, CommentMapper commentMapper) {
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }


    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner().getId())
                .lastBooking(null)
                .nextBooking(null)
                .comments(new HashSet<>())
                .build();
    }

    public Item toItem(ItemDto itemDto) {
        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
        item.setOwner(userRepository.findById(itemDto.getOwner()).get());
        return item;
    }
}
