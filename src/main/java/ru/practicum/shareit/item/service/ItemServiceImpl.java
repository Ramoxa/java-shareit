package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public List<ItemDto> getAll(Long userId) {
        List<ItemDto> items = new ArrayList<>();
        for (Item item : itemStorage.getAllByUser(userId)) {
            items.add(ItemMapper.toItemDto(item));
        }
        return items;
    }

    @Override
    public ItemDto findById(Long id) {
        Item item = itemStorage.findById(id).orElseThrow(() -> new NotFoundException("Нет вещи с ID: " + id));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        UserDto user = userService.getById(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(UserMapper.toUser(user));
        itemStorage.create(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long id, Long userId) {
        Item existingItem = itemStorage.findById(id).orElseThrow(() -> new NotFoundException("Нет вещи с ID: " + id));

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The item doesn't belong to the user.");
        }

        Item updatedItem = mergeItems(existingItem, itemDto);
        itemStorage.update(updatedItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public void delete(Long id) {
        Item item = itemStorage.findById(id).orElseThrow(() -> new NotFoundException("Нет вещи с ID: " + id));
        itemStorage.delete(id);
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemStorage.findAll().stream().filter(i -> isSearched(text, i)).map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private Boolean isSearched(String text, Item item) {
        return (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())) && item.getAvailable();
    }

    private Item mergeItems(Item existingItem, ItemDto newItemDto) {
        if (newItemDto.getName() != null) {
            existingItem.setName(newItemDto.getName());
        }
        if (newItemDto.getDescription() != null) {
            existingItem.setDescription(newItemDto.getDescription());
        }
        if (newItemDto.getAvailable() != null) {
            existingItem.setAvailable(newItemDto.getAvailable());
        }
        return existingItem;
    }
}