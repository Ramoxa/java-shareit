package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserCreateDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private UserClient mockedUserClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("create correct user")
    void createUserTest() throws Exception {
        long userId = 2;
        String userName = "Anna";
        String userEmail = "anna@mail.ru";
        UserCreateDTO userCreateDTO = new UserCreateDTO(userName, userEmail);
        String json = objectMapper.writeValueAsString(userCreateDTO);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.CREATED);
        when(mockedUserClient.createUser(userCreateDTO)).thenReturn(response);
        mockMvc.perform(post("/users").contentType("application/json").content(json))
                .andExpect(status().isCreated());
        verify(mockedUserClient, times(1)).createUser(userCreateDTO);
        verifyNoMoreInteractions(mockedUserClient);
    }

    @Test
    @DisplayName("update correct user")
    void updateCorrectUserTest() throws Exception {
        long userId = 1231L;
        String userName = "Anna";
        String userEmail = "anna@mail.ru";
        UserUpdateDTO updateDTO = new UserUpdateDTO(userName, userEmail);
        String json = objectMapper.writeValueAsString(updateDTO);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockedUserClient.updateUser(userId, updateDTO)).thenReturn(response);

        mockMvc.perform(patch("/users/" + userId).contentType("application/json").content(json))
                .andExpect(status().isOk());
        verify(mockedUserClient, times(1)).updateUser(userId, updateDTO);
        verifyNoMoreInteractions(mockedUserClient);
    }

    @Test
    @DisplayName("update correct user")
    void updateIncorrectUserTest() throws Exception {
        Long userId = null;
        String userName = "Anna";
        String userEmail = "anna@mail.ru";
        UserUpdateDTO updateDTO = new UserUpdateDTO(userName, userEmail);
        String json = objectMapper.writeValueAsString(updateDTO);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockedUserClient.updateUser(userId, updateDTO)).thenReturn(response);

        mockMvc.perform(patch("/users/" + userId).contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("delete correct user")
    void deleteUserTest() throws Exception {
        long userId = 100L;
        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNoContent());
        verify(mockedUserClient, times(1)).deleteUser(userId);
        verifyNoMoreInteractions(mockedUserClient);
    }

    @Test
    @DisplayName("delete correct user")
    void deleteIncorrectUserTest() throws Exception {
        Long userId = null;
        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("get correct user")
    void getCorrectUser() throws Exception {
        long userId = 1213;
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockedUserClient.getUser(userId)).thenReturn(response);

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk());
        verify(mockedUserClient, times(1)).getUser(userId);
        verifyNoMoreInteractions(mockedUserClient);
    }

    @Test
    @DisplayName("get correct user")
    void getIncorrectUser() throws Exception {
        Long userId = null;
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockedUserClient.getUser(userId)).thenReturn(response);
        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isBadRequest());
        verify(mockedUserClient, times(0)).getUser(userId);
        verifyNoMoreInteractions(mockedUserClient);
    }

    @Test
    @DisplayName("get all users")
    void getAllUsers() throws Exception {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockedUserClient.getAllUsers()).thenReturn(response);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
        verify(mockedUserClient, times(1)).getAllUsers();
        verifyNoMoreInteractions(mockedUserClient);
    }
}