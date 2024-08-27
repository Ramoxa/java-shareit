package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final String header = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemInfoDto> findAll(@RequestHeader(header) Long userId) {
        log.info("Find all items");
        return itemService.findItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto getItemDto(@RequestHeader(header) Long userId,
                                  @PathVariable Long itemId) {
        log.info("Get item dto");
        return itemService.findItemById(userId, itemId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(header) Long userId,
                           @RequestBody ItemDto itemDto) {
        log.info("Create item");
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(header) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("Update item");
        return itemService.update(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(header) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentRequestDto commentRequestDto) {
        log.info("Add comment");
        return itemService.addComment(userId, itemId, commentRequestDto);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItemDto(@RequestParam(defaultValue = "", required = false) String text) {
        log.info("Search items");
        return itemService.findItemsByText(text);
    }
}
