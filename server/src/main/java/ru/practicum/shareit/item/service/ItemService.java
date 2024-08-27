package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.Collection;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemInfoDto findItemById(Long userId, Long itemId);

    Collection<ItemInfoDto> findItemsByUserId(Long userId);

    Collection<ItemDto> findItemsByText(String text);

    CommentDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto);
}
