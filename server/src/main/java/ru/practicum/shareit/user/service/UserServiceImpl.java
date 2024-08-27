package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.mapper.UserDTOMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.Validator;

import java.util.Collection;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Validator validator;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        validator.validateIfEmailIsUnique(userCreateDTO.getEmail());
        User userToCreate = UserDTOMapper.fromCreateDTO(userCreateDTO);
        User createdUser = userRepository.save(userToCreate);
        log.info("{} was created", createdUser);
        return UserDTOMapper.toDTO(createdUser);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public UserDTO updateUser(long userId, UserUpdateDTO userUpdateDTO) {
        validator.validateIfUserNotExists(userId);
        String email = userUpdateDTO.getEmail();
        if (nonNull(userUpdateDTO.getEmail())) {
            validator.validateIfEmailIsUnique(email, userId);
        }
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException(String.format("User with id=%d does not exists", userId)));

        User userToUpdate = fillInFieldsToUpdate(oldUser, userUpdateDTO);
        User updatedUser = userRepository.save(userToUpdate);
        log.info("{} was updated", updatedUser);
        return UserDTOMapper.toDTO(updatedUser);
    }

    private User fillInFieldsToUpdate(User user, UserUpdateDTO updateUserDTO) {
        String email = updateUserDTO.getEmail();
        if (nonNull(email)) {
            user.setEmail(email);
        }
        String name = updateUserDTO.getName();
        if (nonNull(name)) {
            user.setName(name);
        }
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        validator.validateIfUserNotExists(userId);
        userRepository.deleteById(userId);
        log.info("User with id={} deleted", userId);
    }

    @Override
    public UserDTO getUser(long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(UserDTOMapper::toDTO).orElseThrow(
                () -> new IdNotFoundException("User with id=" + userId + " not found"));
    }

    @Override
    public Collection<UserDTO> getAllUsers() {
        Collection<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDTOMapper::toDTO)
                .toList();
    }
}
