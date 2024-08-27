package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> findAll();

    UserDto create(UserDto userDto);

    UserDto update(Long userId, UserDto userDto);

    UserDto getUserDtoById(Long userId);

    void delete(Long userId);
}
