package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> userItemIndex = new LinkedHashMap<>();

    private Long id = 1L;

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getAllByUser(Long id) {
        return userItemIndex.getOrDefault(id, List.of());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item create(Item item) {
        item.setId(id);
        id++;
        items.put(item.getId(), item);
        final List<Item> items = userItemIndex.computeIfAbsent(item.getOwner().getId(), k -> new ArrayList<>());
        items.add(item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
    }
}