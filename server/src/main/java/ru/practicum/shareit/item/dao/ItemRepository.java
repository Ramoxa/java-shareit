package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findAllByOwnerIdOrderByIdAsc(Long ownerId);

    @Query("select i from Item i where (lower(i.name) like lower(concat('%', ?1,'%')) or lower(i.description) like lower(concat('%', ?1,'%'))) and i.available = true")
    Collection<Item> findByNameOrDescriptionContainingIgnoreCase(String text);

    Collection<Item> getItemsByRequestId(Long requestId, Sort sort);
}
