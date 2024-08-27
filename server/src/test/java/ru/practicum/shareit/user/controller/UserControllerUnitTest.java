package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ObjectAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserCreateDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerUnitTest {
    @MockBean
    private UserService mockedUserService;

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
        UserDTO userSavedDTO = new UserDTO(userId, userName, userEmail);
        when(mockedUserService.createUser(userCreateDTO)).thenReturn(userSavedDTO);
        String json = objectMapper.writeValueAsString(userCreateDTO);

        mockMvc.perform(post("/users").contentType("application/json").content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(is(userId), Long.class))
                .andExpect(jsonPath("$.name").value(is(userName), String.class))
                .andExpect(jsonPath("$.email").value(is(userEmail), String.class));
        verify(mockedUserService, times(1)).createUser(userCreateDTO);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    @DisplayName("create user with incorrect email")
    void createIncorrectUserTest() throws Exception {
        String userName = "Anna";
        String userBadEmail = "annamail.ru";
        UserCreateDTO userCreateDTO = new UserCreateDTO(userName, userBadEmail);
        String json = objectMapper.writeValueAsString(userCreateDTO);
        when(mockedUserService.createUser(userCreateDTO)).thenThrow(ObjectAlreadyExistsException.class);

        mockMvc.perform(post("/users").contentType("application/json").content(json))
                .andExpect(status().isConflict());
        verify(mockedUserService, times(1)).createUser(userCreateDTO);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    @DisplayName("update correct user")
    void updateCorrectUserTest() throws Exception {
        long userId = 1231L;
        String userName = "Anna";
        String userEmail = "anna@mail.ru";
        UserUpdateDTO updateDTO = new UserUpdateDTO(userName, userEmail);
        String json = objectMapper.writeValueAsString(updateDTO);
        UserDTO userDTO = new UserDTO(userId, userName, userEmail);
        when(mockedUserService.updateUser(userId, updateDTO)).thenReturn(userDTO);

        mockMvc.perform(patch("/users/" + userId).contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(userId), Long.class))
                .andExpect(jsonPath("$.name").value(is(userName), String.class))
                .andExpect(jsonPath("$.email").value(is(userEmail), String.class));

        verify(mockedUserService, times(1)).updateUser(userId, updateDTO);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    @DisplayName("delete correct user")
    void deleteUserTest() throws Exception {
        long userId = 100L;
        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNoContent());
        verify(mockedUserService, times(1)).deleteUser(userId);
        verifyNoMoreInteractions(mockedUserService);
    }

    @Test
    @DisplayName("get correct user")
    void getCorrectUser() throws Exception {
        long userId = 1213L;
        String userName = "Anna";
        String userEmail = "aaa@mail.ru";
        UserDTO userDTO = new UserDTO(userId, userName, userEmail);
        when(mockedUserService.getUser(userId)).thenReturn(userDTO);

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(userId), Long.class))
                .andExpect(jsonPath("$.name").value(is(userName), String.class))
                .andExpect(jsonPath("$.email").value(is(userEmail), String.class));
    }

    @Test
    @DisplayName("get all users")
    void getAllUsers() throws Exception {
        UserDTO userDTO1 = new UserDTO(21L, "andrew", "andrew@mail.ru");
        UserDTO userDTO2 = new UserDTO(121L, "maria", "maria@mail.ru");
        UserDTO userDTO3 = new UserDTO(21221L, "petr", "petr@mail.ru");
        Collection<UserDTO> userDTOs = List.of(userDTO1, userDTO2, userDTO3);
        when(mockedUserService.getAllUsers()).thenReturn(userDTOs);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(is((int) userDTOs.size())));
    }
}
