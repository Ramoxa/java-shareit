package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final String header = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(header) Long userId,
                                         @Valid @RequestBody ItemRequestCreateDto itemRequestRequestDto) {
        return itemRequestClient.create(userId, itemRequestRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(header) Long userId) {
        return itemRequestClient.findAll(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findItemRequestById(@RequestHeader(header) Long userId,
                                                      @PathVariable Long requestId) {
        return itemRequestClient.findItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllUsersItemRequest(
            @RequestHeader(header) Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
            @Positive @RequestParam(defaultValue = "10", required = false) int size) {
        return itemRequestClient.findAllUsersItemRequest(userId, from, size);
    }
}