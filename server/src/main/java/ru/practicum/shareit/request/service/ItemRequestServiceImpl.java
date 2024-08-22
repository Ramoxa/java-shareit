package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemRequestDto> findAllByUserId(Long userId) {
        return itemRequestRepository.findAllByRequestorId(userId).stream().map(ItemRequestMapper::toItemRequestDto).toList();
    }

    @Override
    public ItemRequestDto create(ItemRequestCreateDto itemRequestRequestDto) {
        ItemRequest itemRequest = itemRequestRepository.save(
                ItemRequest.builder()
                        .description(itemRequestRequestDto.getDescription())
                        .requestor(userRepository.findById(itemRequestRequestDto.getRequestorId())
                                .orElseThrow(() -> new NotFoundException("User id = " + itemRequestRequestDto.getRequestorId() + " not found!")))
                        .created(LocalDateTime.now())
                        .build()
        );
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto findItemRequestById(Long itemRequestId) {
        log.info(itemRequestRepository.findById(itemRequestId).toString());
        return ItemRequestMapper.toItemRequestDto(
                itemRequestRepository.findById(itemRequestId)
                        .orElseThrow(() -> new NotFoundException("ItemRequest id = " + itemRequestId + " not found!")));
    }

    @Override
    public List<ItemRequestDto> findAllUsersItemRequest(Pageable pageable) {
        return itemRequestRepository.findAll(pageable).get()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }
}