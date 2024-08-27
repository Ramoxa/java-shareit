package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDTO;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestClient mockItemRequestClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String userIdHeader = "X-Sharer-User-Id";

    @Test
    @DisplayName("create request for item")
    void createRequest() throws Exception {
        long userId = 1231L;
        String description = "bla-bla";
        ItemRequestCreateDTO itemRequestCreateDTO = new ItemRequestCreateDTO();
        itemRequestCreateDTO.setDescription(description);
        String json = objectMapper.writeValueAsString(itemRequestCreateDTO);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.CREATED);

        when(mockItemRequestClient.createItemRequest(userId, itemRequestCreateDTO)).thenReturn(response);

        mockMvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(json)
                        .header(userIdHeader, userId))
                .andExpect(status().isCreated());
        verify(mockItemRequestClient, times(1)).createItemRequest(userId, itemRequestCreateDTO);
        verifyNoMoreInteractions(mockItemRequestClient);
    }

    @Test
    @DisplayName("get requests from user")
    void getUserRequests() throws Exception {
        long userId = 1000L;
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockItemRequestClient.getUserRequests(userId)).thenReturn(response);

        mockMvc.perform(get("/requests").header(userIdHeader, userId))
                .andExpect(status().isOk());
        verify(mockItemRequestClient, times(1)).getUserRequests(userId);
        verifyNoMoreInteractions(mockItemRequestClient);
    }

    @Test
    @DisplayName("get all requests")
    void getAllRequestsExceptUser() throws Exception {
        long userId = 1000L;
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockItemRequestClient.getAllRequestsExceptUser(userId)).thenReturn(response);

        mockMvc.perform(get("/requests/all").header(userIdHeader, userId))
                .andExpect(status().isOk());
        verify(mockItemRequestClient, times(1)).getAllRequestsExceptUser(userId);
        verifyNoMoreInteractions(mockItemRequestClient);
    }

    @Test
    void getRequestById() throws Exception {
        long userId = 1000L;
        long requestId = 1000L;
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(mockItemRequestClient.getRequestById(userId, requestId)).thenReturn(response);

        mockMvc.perform(get("/requests/" + requestId).header(userIdHeader, userId))
                .andExpect(status().isOk());
        verify(mockItemRequestClient, times(1)).getRequestById(userId, requestId);
        verifyNoMoreInteractions(mockItemRequestClient);
    }

}