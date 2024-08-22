package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import java.util.List;

public interface ItemRequestService {
    List<ItemRequestDto> findAllByUserId(Long userId);

    ItemRequestDto create(ItemRequestCreateDto itemRequestRequestDto);

    ItemRequestDto findItemRequestById(Long itemRequestId);

    List<ItemRequestDto> findAllUsersItemRequest(Pageable pageable);
}