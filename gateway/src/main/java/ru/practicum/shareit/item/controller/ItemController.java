package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.comment.CommentCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemUpdateDTO;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                             @RequestBody @Valid ItemCreateDTO itemCreateDTO) {
        log.info("Gateway: Received request from userId={} to add item with parameters {}", userId, itemCreateDTO);
        return itemClient.createItem(userId, itemCreateDTO);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                              @PathVariable @Positive long itemId,
                              @RequestBody @Valid ItemUpdateDTO itemUpdateDTO) {
        log.info("Gateway: Received request from userId={} to update item with id={} and parameters {}", userId, itemId, itemUpdateDTO);
        return itemClient.updateItem(userId, itemId, itemUpdateDTO);
    }


    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                       @PathVariable @Positive long itemId) {
        log.info("Gateway: Received request from userId={} to get item with id= {}", userId, itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllItems(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("Gateway: Received request from userId={} to get all items", userId);
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                           @RequestParam("text") String text) {
        log.info("Gateway: Received request from userId={} to get items with text={}", userId, text);
        return itemClient.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                 @PathVariable @Positive long itemId,
                                 @RequestBody @Valid CommentCreateDTO comment) {
        log.info("Gateway: Received request from userId={} to add comment {} to itemId={}", userId, comment, itemId);
        return itemClient.addComment(userId, itemId, comment);
    }
}
