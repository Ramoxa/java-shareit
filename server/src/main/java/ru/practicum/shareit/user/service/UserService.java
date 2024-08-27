package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;

import java.util.Collection;

public interface UserService {

    UserDTO createUser(UserCreateDTO user);

    UserDTO updateUser(long userId, UserUpdateDTO user);

    void deleteUser(long userId);

    UserDTO getUser(long userId);

    Collection<UserDTO> getAllUsers();

}
