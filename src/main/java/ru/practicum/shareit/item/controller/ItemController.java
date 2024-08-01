package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.Create;

import java.util.Collections;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting all items for user: {}", userId);
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable Long id) {
        log.info("Getting item by id: {}", id);
        return itemService.findById(id);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Creating new item for user: {}", userId);
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Updating item with id: {} for user: {}", id, userId);
        return itemService.update(itemDto, id, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting item with id: {}", id);
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Searching for items with text: {}", text);
        if (!text.isBlank()) {
            return itemService.search(text);
        } else {
            return Collections.emptyList();
        }
    }
}