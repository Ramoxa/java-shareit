package ru.practicum.shareit.request.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.item.ItemDTOForRequest;
import ru.practicum.shareit.item.mapper.ItemDTOMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTOWithAnswers;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserDTOMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RequestDTOMapper {
    private final ItemRequestRepository requestRepository;
    private final ItemRepository itemRepository;

    public static ItemRequest fromCreateDTO(User user, ItemRequestCreateDTO createDTO) {
        return ItemRequest.builder()
                .description(createDTO.getDescription())
                .requestor(user)
                .build();
    }

    public static ItemRequestDTO toDTO(ItemRequest itemRequest) {
        UserDTO userDTO = UserDTOMapper.toDTO(itemRequest.getRequestor());
        return ItemRequestDTO.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }


    private static ItemRequestDTOWithAnswers toSemiFinishedDTOWithAnswers(ItemRequest itemRequest) {
        return ItemRequestDTOWithAnswers.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public ItemRequestDTOWithAnswers toDTOWithAnswers(ItemRequest itemRequest) {
        ItemRequestDTOWithAnswers request = toSemiFinishedDTOWithAnswers(itemRequest);
        List<ItemDTOForRequest> requestedItemsDTO = itemRepository
                .findByRequestId(itemRequest.getId())
                .stream()
                .map(ItemDTOMapper::toDTOForRequest)
                .toList();
        request.setItems(requestedItemsDTO);
        return request;
    }

    public List<ItemRequestDTOWithAnswers> toDTOWithAnswers(List<ItemRequest> itemRequests) {
        Map<Long, List<ItemDTOForRequest>> itemMap = getItemMapByRequestId(itemRequests);
        return itemRequests.stream()
                .map(RequestDTOMapper::toSemiFinishedDTOWithAnswers)
                .peek(requestDto -> requestDto.setItems(itemMap.get(requestDto.getId())))
                .toList();
    }

    private Map<Long, List<ItemDTOForRequest>> getItemMapByRequestId(List<ItemRequest> itemRequests) {
        List<Long> requestIds = itemRequests.stream().map(ItemRequest::getId).toList();
        List<Item> items = itemRepository.findByRequestId(requestIds);
        Map<Long, List<ItemDTOForRequest>> map = new HashMap<>();
        for (Item item : items) {
            long requestId = item.getRequest().getId();
            if (!map.containsKey(requestId)) {
                map.put(requestId, new ArrayList<>());
            }
            map.get(requestId).add(ItemDTOMapper.toDTOForRequest(item));
        }
        return map;
    }
}
