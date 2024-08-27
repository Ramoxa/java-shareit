package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.exception.ObjectAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserCreateDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.Validator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private Validator mockValidator;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("create new correct User")
    void createUserTest() {
        long userId = 1212L;
        String name = "Andrei";
        String email = "andrew@gmail.com";
        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .name(name)
                .email(email)
                .build();
        User userToSave = User.builder()
                .email(email)
                .name(name)
                .build();
        User savedUser = userToSave.toBuilder().build();
        savedUser.setId(userId);
        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .email(email)
                .name(name)
                .build();
        doNothing().when(mockValidator).validateIfEmailIsUnique(email);
        when(mockUserRepository.save(userToSave)).thenReturn(savedUser);
        InOrder inOrder = inOrder(mockValidator, mockUserRepository);

        UserDTO returnedUserDTO = userService.createUser(userCreateDTO);

        inOrder.verify(mockValidator, times(1)).validateIfEmailIsUnique(email);
        inOrder.verify(mockUserRepository, times(1)).save(userToSave);
        verifyNoMoreInteractions(mockValidator, mockUserRepository);
        assertEquals(userDTO, returnedUserDTO);
    }

    @Test
    @DisplayName("create new User with existing email")
    void createUserWithExistingEmailTest() {
        String name = "Andrei";
        String email = "andrew@gmail.com";
        UserCreateDTO userCreateDTO = UserCreateDTO.builder()
                .name(name)
                .email(email)
                .build();

        doThrow(ObjectAlreadyExistsException.class).when(mockValidator).validateIfEmailIsUnique(email);
        assertThrows(ObjectAlreadyExistsException.class, () -> userService.createUser(userCreateDTO));
        verify(mockValidator, times(1)).validateIfEmailIsUnique(email);
        verify(mockUserRepository, never()).save(any());
    }

    @Test
    @DisplayName("update User with correct name and email")
    void updateUserByCorrectNameAndEmailTest() {
        long userId = 1212L;
        String oldName = "Andrei";
        String oldEmail = "andrew@gmail.com";
        String newName = "Anna";
        String newEmail = "anna@mail.com";
        UserUpdateDTO updateDTO = UserUpdateDTO.builder()
                .name(newName)
                .email(newEmail)
                .build();
        User oldUser = User.builder()
                .id(userId)
                .name(oldName)
                .email(oldEmail)
                .build();
        User userToUpdate = User.builder()
                .id(userId)
                .name(newName)
                .email(newEmail)
                .build();
        User updatedUser = userToUpdate.toBuilder().build();
        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .email(newEmail)
                .name(newName)
                .build();
        doNothing().when(mockValidator).validateIfUserNotExists(userId);
        doNothing().when(mockValidator).validateIfEmailIsUnique(newEmail, userId);
        when(mockUserRepository.findById(userId)).thenReturn(Optional.ofNullable(oldUser));
        when(mockUserRepository.save(userToUpdate)).thenReturn(updatedUser);
        InOrder inOrder = inOrder(mockValidator, mockUserRepository);

        UserDTO returnedUserDTO = userService.updateUser(userId, updateDTO);

        inOrder.verify(mockValidator, times(1)).validateIfUserNotExists(userId);
        inOrder.verify(mockValidator, times(1)).validateIfEmailIsUnique(newEmail, userId);
        inOrder.verify(mockUserRepository, times(1)).findById(userId);
        inOrder.verify(mockUserRepository, times(1)).save(userToUpdate);
        verifyNoMoreInteractions(mockValidator, mockUserRepository);
        assertEquals(userDTO, returnedUserDTO);
    }

    @Test
    @DisplayName("update User with correct name ")
    void updateUserByCorrectNameTest() {
        long userId = 1212L;
        String oldName = "Andrei";
        String email = "andrew@gmail.com";
        String newName = "Anna";
        UserUpdateDTO updateDTO = UserUpdateDTO.builder()
                .name(newName)
                .email(email)
                .build();
        User oldUser = User.builder()
                .id(userId)
                .name(oldName)
                .email(email)
                .build();
        User userToUpdate = User.builder()
                .id(userId)
                .name(newName)
                .email(email)
                .build();
        User updatedUser = userToUpdate.toBuilder().build();
        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .email(email)
                .name(newName)
                .build();
        doNothing().when(mockValidator).validateIfUserNotExists(userId);
        doNothing().when(mockValidator).validateIfEmailIsUnique(email, userId);
        when(mockUserRepository.findById(userId)).thenReturn(Optional.ofNullable(oldUser));
        when(mockUserRepository.save(userToUpdate)).thenReturn(updatedUser);
        InOrder inOrder = inOrder(mockValidator, mockUserRepository);

        UserDTO returnedUserDTO = userService.updateUser(userId, updateDTO);

        inOrder.verify(mockValidator, times(1)).validateIfUserNotExists(userId);
        inOrder.verify(mockValidator, times(1)).validateIfEmailIsUnique(email, userId);
        inOrder.verify(mockUserRepository, times(1)).findById(userId);
        inOrder.verify(mockUserRepository, times(1)).save(userToUpdate);
        verifyNoMoreInteractions(mockValidator, mockUserRepository);
        assertEquals(userDTO, returnedUserDTO);
    }

    @Test
    @DisplayName("update User with correct email")
    void updateUserByCorrectEmailTest() {
        long userId = 1212L;
        String name = "Andrei";
        String oldEmail = "andrew@gmail.com";
        String newEmail = "anna@mail.com";
        UserUpdateDTO updateDTO = UserUpdateDTO.builder()
                .name(name)
                .email(newEmail)
                .build();
        User oldUser = User.builder()
                .id(userId)
                .name(name)
                .email(oldEmail)
                .build();
        User userToUpdate = User.builder()
                .id(userId)
                .name(name)
                .email(newEmail)
                .build();
        User updatedUser = userToUpdate.toBuilder().build();
        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .email(newEmail)
                .name(name)
                .build();
        doNothing().when(mockValidator).validateIfUserNotExists(userId);
        doNothing().when(mockValidator).validateIfEmailIsUnique(newEmail, userId);
        when(mockUserRepository.findById(userId)).thenReturn(Optional.ofNullable(oldUser));
        when(mockUserRepository.save(userToUpdate)).thenReturn(updatedUser);
        InOrder inOrder = inOrder(mockValidator, mockUserRepository);

        UserDTO returnedUserDTO = userService.updateUser(userId, updateDTO);

        inOrder.verify(mockValidator, times(1)).validateIfUserNotExists(userId);
        inOrder.verify(mockValidator, times(1)).validateIfEmailIsUnique(newEmail, userId);
        inOrder.verify(mockUserRepository, times(1)).findById(userId);
        inOrder.verify(mockUserRepository, times(1)).save(userToUpdate);
        verifyNoMoreInteractions(mockValidator, mockUserRepository);
        assertEquals(userDTO, returnedUserDTO);
    }

    @Test
    @DisplayName("update User with incorrect id")
    void updateUserWithIncorrectIdTest() {
        long userId = 1212L;
        String newName = "Anna";
        String newEmail = "anna@mail.com";
        UserUpdateDTO updateDTO = UserUpdateDTO.builder()
                .name(newName)
                .email(newEmail)
                .build();
        doThrow(ObjectAlreadyExistsException.class).when(mockValidator).validateIfUserNotExists(userId);
        assertThrows(ObjectAlreadyExistsException.class, () -> userService.updateUser(userId, updateDTO));
        verify(mockValidator, times(1)).validateIfUserNotExists(userId);
        verifyNoMoreInteractions(mockValidator, mockUserRepository);
    }

    @Test
    @DisplayName("delete correct user")
    void deleteCorrectUserTest() {
        long userId = 1212L;
        doNothing().when(mockValidator).validateIfUserNotExists(userId);
        doNothing().when(mockUserRepository).deleteById(userId);
        InOrder inOrder = inOrder(mockValidator, mockUserRepository);

        userService.deleteUser(userId);

        inOrder.verify(mockValidator, times(1)).validateIfUserNotExists(userId);
        inOrder.verify(mockUserRepository, times(1)).deleteById(userId);
        verifyNoMoreInteractions(mockValidator, mockUserRepository);
    }

    @Test
    @DisplayName("delete incorrect user")
    void deleteIncorrectUserTest() {
        long userId = 1212L;
        doThrow(IdNotFoundException.class).when(mockValidator).validateIfUserNotExists(userId);

        assertThrows(IdNotFoundException.class, () -> userService.deleteUser(userId));
        verify(mockValidator, times(1)).validateIfUserNotExists(userId);
        verifyNoMoreInteractions(mockValidator, mockUserRepository);
    }

    @Test
    @DisplayName("get existing user")
    void getExistingUser() {
        long userId = 1212L;
        String name = "Andrei";
        String email = "andrew@gmail.com";
        User user = User.builder()
                .id(userId)
                .name(name)
                .email(email)
                .build();
        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .email(email)
                .name(name)
                .build();
        when(mockUserRepository.findById(userId)).thenReturn(Optional.ofNullable(user));

        UserDTO returnUserDTO = userService.getUser(userId);

        assertEquals(userDTO, returnUserDTO);
        verify(mockUserRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(mockValidator, mockUserRepository);
    }

    @Test
    @DisplayName("get non-existing user")
    void getNonExistingUserTest() {
        long userId = 1212L;
        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> userService.getUser(userId));
        verify(mockUserRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(mockValidator, mockUserRepository);
    }


    @Test
    @DisplayName("get existing users")
    void getAllUsersTest() {
        User user1 = new User(21L, "andrew", "andrew@mail.ru");
        User user2 = new User(121L, "maria", "maria@mail.ru");
        User user3 = new User(21221L, "petr", "petr@mail.ru");
        List<User> users = List.of(user1, user2, user3);
        UserDTO userDTO1 = new UserDTO(21L, "andrew", "andrew@mail.ru");
        UserDTO userDTO2 = new UserDTO(121L, "maria", "maria@mail.ru");
        UserDTO userDTO3 = new UserDTO(21221L, "petr", "petr@mail.ru");
        Collection<UserDTO> userDTOs = List.of(userDTO1, userDTO2, userDTO3);
        when(mockUserRepository.findAll()).thenReturn(users);

        Collection<UserDTO> returnedUserDTOs = userService.getAllUsers();

        verify(mockUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(mockValidator, mockUserRepository);
        assertEquals(userDTOs.size(), returnedUserDTOs.size());
        assertTrue(returnedUserDTOs.containsAll(userDTOs));
    }

    @Test
    @DisplayName("get users when empty")
    void getUsersWhenEmptyTest() {
        when(mockUserRepository.findAll()).thenReturn(Collections.<User>emptyList());

        Collection<UserDTO> returnedUserDTOs = userService.getAllUsers();

        verify(mockUserRepository, times(1)).findAll();
        verifyNoMoreInteractions(mockValidator, mockUserRepository);
        assertEquals(0, returnedUserDTOs.size());
    }

}