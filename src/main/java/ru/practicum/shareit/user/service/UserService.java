package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto user);

    UserDto editUser(UserDto userDto, Long userId);

    UserDto getUser(Long userId);

    List<UserDto> getUsers();

    UserDto deleteUser(Long userId);
}
