package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IsBlankException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemUpdate;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository repository;

    @Autowired
    public ItemServiceImpl(UserService userService, ItemRepository repository) {
        this.userService = userService;
        this.repository = repository;
    }

    @Override
    public List<ItemDto> getAllByUserId(long userId) {
        userService.validateUserId(userId);
        return repository.findByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(long id) {
        validateItemId(id);
        return ItemMapper.toItemDto(repository.findById(id).get());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.equals("")) {
            return new ArrayList<>();
        }
        return repository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
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
        ItemDto itemDto = ItemMapper.toItemDto(repository.save(item));
        log.info("Item with id {} has been updated", itemId);
        return itemDto;
    }

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        userService.validateUserId(userId);
        itemDto.setOwner(userId);
        ItemDto returnedItemDto = ItemMapper.toItemDto(repository.save(ItemMapper.toItem(itemDto)));
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
        List<Item> items = new ArrayList<>(repository.findByOwner(userId));
        items.stream()
                .filter(i -> i.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("This user with id " + userId + " doesn't have item with id "
                        + itemId));
    }
}
