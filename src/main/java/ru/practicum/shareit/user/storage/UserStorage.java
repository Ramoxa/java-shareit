package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    User update(User user);

    List<User> findAll();

    Optional<User> findById(Long userId);

    void delete(Long id);

}
