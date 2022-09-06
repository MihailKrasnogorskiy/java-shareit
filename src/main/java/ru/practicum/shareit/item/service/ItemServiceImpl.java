package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.OffsetLimitPageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.CommentatorValidationException;
import ru.practicum.shareit.exception.IsBlankException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemUpdate;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository repository;
    private final CommentRepository commentRepository;
    @Lazy
    private final ItemMapper itemMapper;
    @Lazy
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    @Autowired
    @Lazy
    private BookingService bookingService;

    @Autowired
    public ItemServiceImpl(UserService userService, ItemRepository repository, CommentRepository commentRepository,
                           ItemMapper itemMapper, BookingMapper bookingMapper, CommentMapper commentMapper) {
        this.userService = userService;
        this.repository = repository;
        this.commentRepository = commentRepository;
        this.itemMapper = itemMapper;
        this.bookingMapper = bookingMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public List<ItemDto> getAllByUserId(long userId) {
        userService.validateUserId(userId);
        return repository.findByOwner_id(userId).stream()
                .map(itemMapper::toItemDto)
                .map(this::fillBookingInItemDto)
                .map(this::addCommentsToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getAllByUserId(long userId, Integer from, Integer size) {
        userService.validateUserId(userId);
        Pageable pageable = OffsetLimitPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return repository.findByOwner_id(userId, pageable).stream()
                .map(itemMapper::toItemDto)
                .map(this::fillBookingInItemDto)
                .map(this::addCommentsToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(long id, long userId) {
        validateItemId(id);
        userService.validateUserId(userId);
        ItemDto itemDto = itemMapper.toItemDto(repository.findById(id).get());
        if (itemDto.getOwner() == userId) {
            return addCommentsToItemDto(fillBookingInItemDto(itemDto));
        }
        return addCommentsToItemDto(itemDto);
    }

    @Override
    public List<ItemDto> search(String text, Integer from, Integer size) {
        if (text.equals("")) {
            return new ArrayList<>();
        }
        Pageable pageable = OffsetLimitPageable.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return repository.search(text, pageable).stream().map(itemMapper::toItemDto).collect(Collectors.toList());
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
    public ItemDto create(long userId, ItemDto itemDto) {
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

    @Override
    public CommentDto addComment(long userId, long itemId, CommentDto commentDto) {
        validateItemId(itemId);
        userService.validateUserId(userId);
        List<Long> itemIds = bookingService.getPastByUser(userId).stream()
                .map(BookingDto::getItem)
                .map(ItemDto::getId)
                .collect(Collectors.toList());
        if (!itemIds.contains(itemId)) {
            throw new CommentatorValidationException();
        }
        Comment comment = commentMapper.toComment(commentDto);
        comment.setAuthor(UserMapper.toUser(userService.getById(userId)));
        comment.setItem(repository.findById(itemId).get());
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
        log.info("Comment with id {} created", comment.getId());
        return commentMapper.toCommentDto(comment);
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

    /**
     * проверка является ли пользователь владельцем
     *
     * @param userId id пользователя
     */
    private void doUserHaveItems(long userId) {
        if (getAllByUserId(userId).isEmpty()) {
            throw new NotFoundException("This user with id " + userId + " doesn't have items");
        }
    }

    /**
     * проверка явсляется ли пользователь владельцем конкретной вещи
     *
     * @param userId id пользователя
     * @param itemId id вещи
     */
    private void doUserHaveThisItems(long userId, long itemId) {
        doUserHaveItems(userId);
        List<Item> items = new ArrayList<>(repository.findByOwner_id(userId));
        items.stream()
                .filter(i -> i.getId() == itemId)
                .findFirst().orElseThrow(() ->
                        new NotFoundException("This user with id " + userId + " doesn't have item with id " + itemId));
    }

    /**
     * добавление бронирований в dto объект вещи
     *
     * @param itemDto dto объект вещи
     * @return dto объект вещи
     */
    private ItemDto fillBookingInItemDto(ItemDto itemDto) {
        long itemId = itemDto.getId();
        List<BookingDto> bookings = bookingService.findAllByItemId(itemId);
        for (int i = bookings.size() - 1; i >= 0; i--) {
            if (bookings.get(i).getStart().isAfter(LocalDateTime.now())) {
                itemDto.setNextBooking(bookingMapper.toBookingForItemDto(bookings.get(i)));
                if (i != bookings.size() - 1) {
                    itemDto.setLastBooking(bookingMapper.toBookingForItemDto(bookings.get(i + 1)));
                }
            }
        }
        return itemDto;
    }

    /**
     * добавление комментариев в dto объект вещи
     *
     * @param itemDto dto объект вещи
     * @return dto объект вещи
     */
    private ItemDto addCommentsToItemDto(ItemDto itemDto) {
        commentRepository.findByItemId(itemDto.getId()).stream()
                .map(commentMapper::toCommentDto)
                .forEach(itemDto.getComments()::add);
        return itemDto;
    }
}
