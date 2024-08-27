package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTOWithAnswers;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDTO createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestBody ItemRequestCreateDTO itemRequestCreateDTO) {
        log.info("Received request from userId={} to create item request with parameters {}", userId, itemRequestCreateDTO);
        return itemRequestService.createItemRequest(userId, itemRequestCreateDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDTOWithAnswers> getUserRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Received request from userId={} to get his/her item requests with answers", userId);
        return itemRequestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDTO> getAllRequestsExceptUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Received request from userId={} to get item requests of all users except his/her", userId);
        return itemRequestService.getAllRequestsExceptUser(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDTOWithAnswers getRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @PathVariable long requestId) {
        log.info("Received request from userId={} to get item request with id={}", userId, requestId);
        return itemRequestService.getRequestById(requestId);
    }
}
