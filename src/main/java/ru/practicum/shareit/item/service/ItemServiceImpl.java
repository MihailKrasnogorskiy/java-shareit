package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.ItemUpdate;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
        return repository.getAllByUserId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(long id) {
        validateItemId(id);
        return ItemMapper.toItemDto(repository.getById(id));
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
        return ItemMapper.toItemDto(repository.update(userId, itemId, itemUpdate));
    }

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        userService.validateUserId(userId);
        return ItemMapper.toItemDto(repository.create(userId, ItemMapper.toItem(itemDto)));
    }

    @Override
    public void delete(long userId, long itemId) {
        userService.validateUserId(userId);
        validateItemId(itemId);
        repository.delete(userId, itemId);
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
}
