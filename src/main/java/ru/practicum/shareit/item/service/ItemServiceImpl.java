package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.IsBlankException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CreatingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemUpdate;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
class ItemServiceImpl implements ItemService {
    private final UserService userService;
    @Autowired
    @Lazy
    private BookingService bookingService;
    private final ItemRepository repository;
    @Lazy
    private final ItemMapper itemMapper;

    @Autowired
    public ItemServiceImpl(UserService userService, ItemRepository repository, ItemMapper itemMapper) {
        this.userService = userService;
        this.repository = repository;
        this.itemMapper = itemMapper;
    }

    @Override
    public List<ItemDtoWithBooking> getAllByUserId(long userId) {
        userService.validateUserId(userId);
        return repository.findByOwner_id(userId).stream()
                .map(itemMapper::toItemDto)
                .map(itemMapper::toItemDtoWithBooking)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(long id, long userId) {
        validateItemId(id);
        userService.validateUserId(userId);
        ItemDto itemDto = itemMapper.toItemDto(repository.findById(id).get());
        if (itemDto.getOwner() == userId) {
            ItemDtoWithBooking itemDtoWithBooking = itemMapper.toItemDtoWithBooking(itemDto);
            System.out.println(itemDto);
            System.out.println(itemDtoWithBooking);
            List<BookingDto> bookings = bookingService.findAllByItemId(id);
            for (int i = bookings.size()-1; i > 0; i--) {
                if (bookings.get(i).getStart().isAfter(LocalDateTime.now())) {
                    itemDtoWithBooking.setNextBooking(bookings.get(i));
                    if (i != bookings.size()-1) {
                        itemDtoWithBooking.setLastBooking(bookings.get(i + 1));
                    }
                }
            }
            return itemDtoWithBooking;
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.equals("")) {
            return new ArrayList<>();
        }
        return repository.search(text).stream().map(itemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemUpdate itemUpdate) {
        userService.validateUserId(userId);
        validateItemId(itemId);
        doUserHaveThisItems(userId, itemId);
        Item item = repository.findById(itemId).get();
        if (itemUpdate.getName() != null && itemUpdate.getName().equals("")) {
            throw new IsBlankException("Item name");
        } else if (itemUpdate.getName() != null) {
            item.setName(itemUpdate.getName());
        }
        if (itemUpdate.getDescription() != null && itemUpdate.getDescription().equals("")) {
            throw new IsBlankException("Item description");
        } else if (itemUpdate.getDescription() != null) {
            item.setDescription(itemUpdate.getDescription());
        }
        if (itemUpdate.getAvailable() != null) {
            item.setAvailable(itemUpdate.getAvailable());
        }
        ItemDto itemDto = itemMapper.toItemDto(repository.save(item));
        log.info("Item with id {} has been updated", itemId);
        return itemDto;
    }

    @Override
    public ItemDto create(long userId, CreatingItemDto itemDto) {
        userService.validateUserId(userId);
        itemDto.setOwner(userId);
        ItemDto returnedItemDto = itemMapper.toItemDto(repository.save(itemMapper.toItem(itemDto)));
        log.info("Item with id {} has been created", returnedItemDto.getId());
        return returnedItemDto;
    }

    @Override
    public void delete(long userId, long itemId) {
        userService.validateUserId(userId);
        validateItemId(itemId);
        doUserHaveThisItems(userId, itemId);
        Item item = repository.findById(itemId).get();
        repository.delete(item);
        log.info("Item with id {} has been deleted", itemId);
    }

    /**
     * проверка существования вещи по id
     *
     * @param id - id проверяемой вещи
     */
    private void validateItemId(long id) {
        if (!repository.getAllItemsId().contains(id)) {
            throw new NotFoundException("Item with id " + id + " not found");
        }
    }

    private void doUserHaveItems(long userId) {
        if (getAllByUserId(userId).isEmpty()) {
            throw new NotFoundException("This user with id " + userId + " doesn't have items");
        }
    }

    private void doUserHaveThisItems(long userId, long itemId) {
        doUserHaveItems(userId);
        List<Item> items = new ArrayList<>(repository.findByOwner_id(userId));
        items.stream().filter(i -> i.getId() == itemId).findFirst().orElseThrow(() -> new NotFoundException("This user with id " + userId + " doesn't have item with id " + itemId));
    }
}
