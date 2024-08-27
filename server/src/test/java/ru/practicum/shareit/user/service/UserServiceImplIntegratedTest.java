package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.exception.ObjectAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserCreateDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceImplIntegratedTest {

    private final UserServiceImpl userService;
    private final EntityManager em;

    @Test
    @DisplayName("create new correct User")
    @Order(1)
    void createUserTest() {
        String name = "Andrei";
        String email = "andrew@gmail.com";
        UserCreateDTO userCreateDTO = new UserCreateDTO(name, email);

        userService.createUser(userCreateDTO);

        TypedQuery<User> getUserQuery = em.createQuery("SELECT u FROM User AS u WHERE u.email=:email", User.class)
                .setParameter("email", email);
        User savedUser = getUserQuery.getSingleResult();
        assertTrue(savedUser.getId() >= 0);
        assertEquals(name, savedUser.getName());
        assertEquals(email, savedUser.getEmail());
    }

    @Test
    @DisplayName("create new User with existing email")
    @Order(2)
    void createUserWithExistingEmailTest() {
        String name = "Andrei";
        String email = "andrew@gmail.com";
        UserCreateDTO userCreateDTO = new UserCreateDTO(name, email);
        String name2 = "Mike";
        UserCreateDTO userCreateDTO2 = new UserCreateDTO(name2, email);
        userService.createUser(userCreateDTO);

        assertThrows(ObjectAlreadyExistsException.class, () -> userService.createUser(userCreateDTO2));
    }

    @Test
    @DisplayName("update User with correct name and email")
    @Order(3)
    void updateUserByCorrectNameAndEmailTest() {
        String oldName = "Andrei";
        String oldEmail = "andrew@gmail.com";
        UserCreateDTO userCreateDTO = new UserCreateDTO(oldName, oldEmail);
        long userId = userService.createUser(userCreateDTO).getId();
        String newName = "Anna";
        String newEmail = "anna@mail.com";
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(newName, newEmail);
        UserDTO toReturn = new UserDTO(userId, newName, newEmail);

        UserDTO returnedDTO = userService.updateUser(userId, userUpdateDTO);

        assertEquals(toReturn, returnedDTO);
        TypedQuery<User> getUserQuery = em.createQuery("SELECT u FROM User AS u WHERE u.id=:userId", User.class)
                .setParameter("userId", userId);
        User savedUser = getUserQuery.getSingleResult();
        assertEquals(userId, savedUser.getId());
        assertEquals(newName, savedUser.getName());
        assertEquals(newEmail, savedUser.getEmail());
    }

    @Test
    @DisplayName("update User with correct name")
    @Order(4)
    void updateUserByCorrectNameTest() {
        String oldName = "Andrei";
        String email = "andrew@gmail.com";
        UserCreateDTO userCreateDTO = new UserCreateDTO(oldName, email);
        long userId = userService.createUser(userCreateDTO).getId();
        String newName = "Anna";
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(newName, email);
        UserDTO toReturn = new UserDTO(userId, newName, email);

        UserDTO returnedDTO = userService.updateUser(userId, userUpdateDTO);

        assertEquals(toReturn, returnedDTO);
        TypedQuery<User> getUserQuery = em.createQuery("SELECT u FROM User AS u WHERE u.id=:userId", User.class)
                .setParameter("userId", userId);
        User savedUser = getUserQuery.getSingleResult();
        assertEquals(userId, savedUser.getId());
        assertEquals(newName, savedUser.getName());
        assertEquals(email, savedUser.getEmail());
    }

    @Test
    @DisplayName("update User with correct email")
    @Order(5)
    void updateUserByCorrectEmailTest() {
        String name = "Andrei";
        String oldEmail = "andrew@gmail.com";
        UserCreateDTO userCreateDTO = new UserCreateDTO(name, oldEmail);
        long userId = userService.createUser(userCreateDTO).getId();
        String newEmail = "anna@mail.com";
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(name, newEmail);
        UserDTO toReturn = new UserDTO(userId, name, newEmail);

        UserDTO returnedDTO = userService.updateUser(userId, userUpdateDTO);

        assertEquals(toReturn, returnedDTO);
        TypedQuery<User> getUserQuery = em.createQuery("SELECT u FROM User AS u WHERE u.id=:userId", User.class)
                .setParameter("userId", userId);
        User savedUser = getUserQuery.getSingleResult();
        assertEquals(userId, savedUser.getId());
        assertEquals(name, savedUser.getName());
        assertEquals(newEmail, savedUser.getEmail());
    }

    @Test
    @DisplayName("update User with incorrect id")
    @Order(6)
    void updateUserWithIncorrectIdTest() {
        long badUserId = 122121L;
        String name = "Andrei";
        String email = "andrew@gmail.com";
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(name, email);

        assertThrows(IdNotFoundException.class, () -> userService.updateUser(badUserId, userUpdateDTO));
    }

    @Test
    @DisplayName("delete correct user")
    @Order(7)
    void deleteCorrectUserTest() {
        String name = "Andrei";
        String email = "andrew@gmail.com";
        UserCreateDTO userCreateDTO = new UserCreateDTO(name, email);
        userService.createUser(userCreateDTO);
        TypedQuery<User> getUserQuery = em.createQuery("SELECT u FROM User AS u WHERE u.email=:email", User.class)
                .setParameter("email", email);
        User savedUser = getUserQuery.getSingleResult();
        long savedUserId = savedUser.getId();
        assertTrue(savedUserId >= 0);
        assertEquals(name, savedUser.getName());
        assertEquals(email, savedUser.getEmail());

        userService.deleteUser(savedUserId);
        TypedQuery<Long> getQuery = em.createQuery("SELECT COUNT(u) FROM User AS u WHERE u.id=:userId", Long.class)
                .setParameter("userId", savedUserId);
        assertEquals(0, getQuery.getSingleResult());
    }

    @Test
    @DisplayName("get existing user")
    @Order(8)
    void getExistingUserTest() {
        String name = "Andrei";
        String email = "andrew@gmail.com";
        UserCreateDTO userCreateDTO = new UserCreateDTO(name, email);
        long userId = userService.createUser(userCreateDTO).getId();

        UserDTO returnedUser = userService.getUser(userId);

        assertEquals(userId, returnedUser.getId());
        assertEquals(name, returnedUser.getName());
        assertEquals(email, returnedUser.getEmail());
    }

    @Test
    @DisplayName("get non-existing user")
    @Order(9)
    void getNonExistingUserTest() {
        assertThrows(IdNotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    @DisplayName("get existing users")
    @Order(10)
    void getAllUsersTest() {
        String name1 = "andrew";
        String email1 = "andrew@mail.ru";
        UserCreateDTO userCreate1 = new UserCreateDTO(name1, email1);
        long userId1 = userService.createUser(userCreate1).getId();
        UserDTO userDTO1 = new UserDTO(userId1, name1, email1);

        String name2 = "maria";
        String email2 = "maria@mail.ru";
        UserCreateDTO userCreate2 = new UserCreateDTO(name2, email2);
        long userId2 = userService.createUser(userCreate2).getId();
        UserDTO userDTO2 = new UserDTO(userId2, name2, email2);

        String name3 = "petr";
        String email3 = "petr@mail.ru";
        UserCreateDTO userCreate3 = new UserCreateDTO(name3, email3);
        long userId3 = userService.createUser(userCreate3).getId();
        UserDTO userDTO3 = new UserDTO(userId3, name3, email3);

        List<UserDTO> userDTOs = List.of(userDTO1, userDTO2, userDTO3);

        Collection<UserDTO> returnedUserDTOs = userService.getAllUsers();

        assertEquals(userDTOs.size(), returnedUserDTOs.size());
        assertTrue(returnedUserDTOs.containsAll(userDTOs));
    }

    @Test
    @DisplayName("get users when empty")
    @Order(11)
    void getUsersWhenEmptyTest() {
        assertEquals(0, userService.getAllUsers().size());
    }


}

