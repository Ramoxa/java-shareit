package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ru.practicum.shareit.comment.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final String header = "X_SHARER_USER_ID";
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(header) Long userId) {
        return itemClient.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader(header) Long userId,
                                          @PathVariable Long itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(header) Long userId,
                                             @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemClient.createItem(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(header) Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody ItemRequestDto itemRequestDto) {
        return itemClient.updateItem(userId, itemId, itemRequestDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(header) Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentRequestDto commentRequestDto) {
        return itemClient.addComment(userId, itemId, commentRequestDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemsByText(
            @RequestHeader(header) Long userId,
            @RequestParam(defaultValue = "", required = false) String text) {
        return itemClient.findItemsByText(userId, text);
    }
}