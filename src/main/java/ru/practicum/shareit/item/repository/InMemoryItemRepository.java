package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemUpdate;

import java.util.*;
import java.util.stream.Collectors;

@Repository
class InMemoryItemRepository implements ItemRepository {

    private final HashMap<Long, List<Item>> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item create(long userId, Item item) {
        item.setId(getId());
        item.setOwner(userId);
        if (items.containsKey(userId)) {
            items.get(userId).add(item);
        } else {
            List<Item> userItems = new ArrayList<>();
            userItems.add(item);
            items.put(userId, userItems);
        }
        return item;
    }

    @Override
    public List<Long> getAllItemsId() {
        return items.values().stream()
                .flatMap(Collection::stream)
                .map(Item::getId)
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(long id) {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public List<Item> getAllByUserId(long userId) {
        doUserHaveItems(userId);
        return items.get(userId);
    }

    @Override
    public Item update(long userId, long itemId, ItemUpdate itemUpdate) {
        Item item = getUserItemByItemId(userId, itemId);
        if (itemUpdate.getAvailable() != null) {
            item.setAvailable(itemUpdate.getAvailable());
        }
        if (itemUpdate.getDescription() != null) {
            item.setDescription(itemUpdate.getDescription());
        }
        if (itemUpdate.getName() != null) {
            item.setName(itemUpdate.getName());
        }
        return item;
    }

    @Override
    public List<Item> search(String text) {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(Item::getAvailable)
                .filter(item -> (item.getName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) ||
                        item.getDescription().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long userId, long itemId) {
        Item item = getUserItemByItemId(userId, itemId);
        items.get(userId).remove(item);
    }

    @Override
    public void clear() {
        items.clear();
        id = 0;
    }

    /**
     * метод генерации id
     *
     * @return сгенерированный id
     */
    private long getId() {
        return ++id;
    }

    private void doUserHaveItems(long userId) {
        if (!items.containsKey(userId)) {
            throw new NotFoundException("This user with id " + userId + " doesn't have items");
        }
    }

    private Item getUserItemByItemId(long userId, long itemId) {
        doUserHaveItems(userId);
        return items.get(userId).stream()
                .filter(i -> i.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("This user with id " + userId + " doesn't have item with id "
                        + itemId));
    }
}