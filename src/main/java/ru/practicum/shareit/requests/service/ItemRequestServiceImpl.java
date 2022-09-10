package ru.practicum.shareit.requests.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.OffsetLimitPageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.dto.CreatedItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final ItemRequestMapper mapper;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository repository, UserService userService,
                                  ItemRepository itemRepository, ItemRequestMapper mapper, ItemMapper itemMapper) {
        this.repository = repository;
        this.userService = userService;
        this.itemRepository = itemRepository;

        this.mapper = mapper;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemRequestDto create(long userId, CreatedItemRequestDto dto) {
        userService.validateUserId(userId);
        ItemRequest request = ItemRequest.builder()
                .created(LocalDateTime.now())
                .requester(UserMapper.toUser(userService.getById(userId)))
                .description(dto.getDescription())
                .build();
        request = repository.save(request);
        log.info("Request for item with id {} has been created", request.getId());
        return mapper.toItemRequestDto(request);
    }

    @Override
    public ItemRequestDto getById(long requestId, long userId) {
        userService.validateUserId(userId);
        validate(requestId);
        ItemRequestDto dto = mapper.toItemRequestDto(repository.findById(requestId).get());
        addItemDto(dto);
        return dto;
    }

    @Override
    public List<ItemRequestDto> findAllByUser(long userId) {
        userService.validateUserId(userId);
        return repository.findItemRequestByRequester_Id(userId).stream()
                .map(mapper::toItemRequestDto)
                .map(this::addItemDto)
                .sorted((o1, o2) -> o2.getCreated().compareTo(o1.getCreated()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> findAllOnPage(long userId, Integer from, Integer size) {
        userService.validateUserId(userId);
        Pageable pageable = OffsetLimitPageable.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        return repository.findItemRequestByRequester_IdNot(userId, pageable).stream()
                .map(mapper::toItemRequestDto)
                .map(this::addItemDto)
                .collect(Collectors.toList());
    }

    private ItemRequestDto addItemDto(ItemRequestDto dto) {
        if (dto.getItems() == null) {
            return dto;
        }
        itemRepository.findAllByRequestId(dto.getId()).stream()
                .map(itemMapper::itemDtoForRequest)
                .forEach(dto.getItems()::add);
        return dto;
    }

    private void validate(long requestId) {
        if (!repository.getAllItemsId().contains(requestId)) {
            throw new NotFoundException("This request not found");
        }
    }
}
