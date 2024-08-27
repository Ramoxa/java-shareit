package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDTO;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                                @RequestBody @Valid ItemRequestCreateDTO itemRequestCreateDTO) {
        log.info("Gateway: Received request from userId={} to create item request with parameters {}", userId, itemRequestCreateDTO);
        return itemRequestClient.createItemRequest(userId, itemRequestCreateDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserRequests(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("Gateway: Received request from userId={} to get his/her item requests with answers", userId);
        return itemRequestClient.getUserRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllRequestsExceptUser(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("Gateway: Received request from userId={} to get item requests of all users except his/her", userId);
        return itemRequestClient.getAllRequestsExceptUser(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                                    @PathVariable @Positive long requestId) {
        log.info("Gateway: Received request from userId={} to get item request with id={}", userId, requestId);
        return itemRequestClient.getRequestById(userId,requestId);
    }
}
