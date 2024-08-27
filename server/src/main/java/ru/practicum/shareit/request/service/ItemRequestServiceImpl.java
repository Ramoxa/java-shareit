package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTOWithAnswers;
import ru.practicum.shareit.request.mapper.RequestDTOMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.Validator;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository requestRepository;
    private final RequestDTOMapper requestDTOMapper;
    private final Validator validator;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ItemRequestDTO createItemRequest(long userId, ItemRequestCreateDTO itemRequestCreateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException(String.format("User with id=%d does not exists", userId)));
        ItemRequest requestToCreate = RequestDTOMapper.fromCreateDTO(user, itemRequestCreateDTO);
        ItemRequest createdItemRequest = requestRepository.save(requestToCreate);
        log.info("item request {} was created by user with id={}", createdItemRequest, userId);
        return RequestDTOMapper.toDTO(createdItemRequest);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public List<ItemRequestDTOWithAnswers> getUserRequests(long userId) {
        validator.validateIfUserNotExists(userId);
        List<ItemRequest> requests = requestRepository.findAllByRequestor(userId);
        return requestDTOMapper.toDTOWithAnswers(requests);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true)
    public List<ItemRequestDTO> getAllRequestsExceptUser(long userId) {
        validator.validateIfUserNotExists(userId);
        List<ItemRequest> requests = requestRepository.findAllExceptRequestor(userId);
        return requests.stream()
                .map(RequestDTOMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDTOWithAnswers getRequestById(long requestId) {
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IdNotFoundException(
                        (String.format("Item request with id=%d is not available", requestId))));
        return requestDTOMapper.toDTOWithAnswers(request);
    }
}
