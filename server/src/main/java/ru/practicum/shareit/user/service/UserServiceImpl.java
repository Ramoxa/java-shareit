package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> findAll() {
        log.info("Find all users");
        return userRepository.findAll().stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public UserDto create(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
           throw new AlreadyExistsException(userDto.getEmail());
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        log.info("Update user {}", userDto);
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new AlreadyExistsException(userDto.getEmail());
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User (id = " + userId + ") not found!"));
        // Проверка на наличие обновления Email
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            user.setEmail(userDto.getEmail());
        }
        // Проверка на наличие обновления name
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserDtoById(Long userId) {
        log.info("Get user {}", userId);
        return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User id = " + userId + " not found!")));
    }

    @Override
    public void delete(Long userId) {
        log.info("Delete user {}", userId);
        userRepository.deleteById(userId);
    }
}