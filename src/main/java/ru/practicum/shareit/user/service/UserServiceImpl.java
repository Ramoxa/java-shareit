package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto create(UserDto userDto) {
        validateEmailUnique(userDto.getEmail());
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.create(user));
    }

    @Override
    public UserDto getById(Long id) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        validateEmailUniqueForUpdate(id, userDto.getEmail());
        User existingUser = userStorage.findById(id).orElseThrow(() -> new NotFoundException("User data cannot be updated. No user with ID: " + id));

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            existingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            existingUser.setName(userDto.getName());
        }

        User updatedUser = userStorage.update(existingUser);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public List<UserDto> findAll() {
        return userStorage.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        userStorage.delete(id);
    }

    public void validateEmailUnique(String email) {
        boolean emailExists = userStorage.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
        if (emailExists) {
            throw new DuplicateEmailException("Email " + email + " already exists.");
        }
    }

    public void validateEmailUniqueForUpdate(Long id, String email) {
        boolean emailExists = userStorage.findAll().stream().anyMatch(user -> user.getEmail().equals(email) && !user.getId().equals(id));
        if (emailExists) {
            throw new DuplicateEmailException("Email " + email + " already exists.");
        }
    }
}
