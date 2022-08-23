package ru.practicum.shareit.requests.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.IsBlankException;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.dto.CreatedItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private ItemRequestRepository repository;
    private UserService userService;
    private ItemRequestMapper mapper;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository repository, UserService userService,
                                  ItemRequestMapper mapper) {
        this.repository = repository;
        this.userService = userService;
        this.mapper = mapper;
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
}
