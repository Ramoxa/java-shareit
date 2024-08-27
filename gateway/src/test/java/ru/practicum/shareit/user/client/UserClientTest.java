package ru.practicum.shareit.user.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserCreateDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserClientTest {

    @Spy
    private UserClient userClient = new UserClient("http://localhost:9090", new RestTemplateBuilder());

    @Test
    void createUser() {
        String path = "";
        String userName = "Anna";
        String userEmail = "anna@mail.ru";
        UserCreateDTO userCreateDTO = new UserCreateDTO(userName, userEmail);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.CREATED);
        assertThrows(Throwable.class, () -> userClient.createUser(userCreateDTO));
        assertThrows(Throwable.class, () -> userClient.post(path, null, null, userCreateDTO));
    }

    @Test
    void updateUser() {
        long userId = 1231L;
        String userName = "Anna";
        String userEmail = "anna@mail.ru";
        String path = String.format("/%d", userId);
        UserUpdateDTO updateDTO = new UserUpdateDTO(userName, userEmail);
        assertThrows(Throwable.class, () -> userClient.updateUser(userId, updateDTO));
        assertThrows(Throwable.class, () -> userClient.post(path, null, null, updateDTO));
    }

    @Test
    void deleteUser() {
        long userId = 1231L;
        String path = String.format("/%d", userId);
        assertThrows(Throwable.class, () -> userClient.deleteUser(userId));
        assertThrows(Throwable.class, () -> userClient.delete(path));
    }

    @Test
    void getUser() {
        long userId = 1231L;
        String path = String.format("/%d", userId);
        assertThrows(Throwable.class, () -> userClient.getUser(userId));
        assertThrows(Throwable.class, () -> userClient.get(path, null, null));
    }

    @Test
    void getAllUsers() {
        String path = "";
        assertThrows(Throwable.class, () -> userClient.getAllUsers());
        assertThrows(Throwable.class, () -> userClient.get(path, null, null));
    }
}