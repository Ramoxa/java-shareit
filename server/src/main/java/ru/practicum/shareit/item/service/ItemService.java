package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.comment.CommentCreateDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.item.ItemCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemDTOWithBookings;
import ru.practicum.shareit.item.dto.item.ItemUpdateDTO;

import java.util.Collection;

public interface ItemService {
    ItemDTO createItem(long userId, ItemCreateDTO itemCreateDTO);

    ItemDTO updateItem(long userId, long itemId, ItemUpdateDTO itemUpdateDTO);

    ItemDTOWithBookings getItem(long userId, long itemId);

    Collection<ItemDTOWithBookings> getAllItems(long userId);

    Collection<ItemDTO> searchItems(String text);

    CommentDTO addComment(long userId, long itemId, CommentCreateDTO comment);
}
