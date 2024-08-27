package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTOWithAnswers;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDTO createItemRequest(long userId, ItemRequestCreateDTO itemRequestCreateDTO);

    List<ItemRequestDTOWithAnswers> getUserRequests(long userId);

    List<ItemRequestDTO> getAllRequestsExceptUser(long userId);

    ItemRequestDTOWithAnswers getRequestById(long requestId);

}
