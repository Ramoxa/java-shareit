package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    List<Item> findAll();

    List<Item> getAllByUser(Long id);

    Optional<Item> findById(Long id);

    Item create(Item item);

    Item update(Item item);

    void delete(Long id);
}
