package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.comment.CommentCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemUpdateDTO;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    private ItemClient itemClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String userIdHeader = "X-Sharer-User-Id";

    @Test
    void createItem() throws Exception {
        long userId = 112L;
        long id = 10L;
        String name = "name";
        String description = "description";
        boolean available = true;
        ItemCreateDTO itemCreateDTO = new ItemCreateDTO(name, description, available, 10L);
        String json = objectMapper.writeValueAsString(itemCreateDTO);

        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.CREATED);
        when(itemClient.createItem(userId, itemCreateDTO)).thenReturn(response);

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(json)
                        .header(userIdHeader, userId))
                .andExpect(status().isCreated());
        verify(itemClient, times(1)).createItem(userId, itemCreateDTO);
        verifyNoMoreInteractions(itemClient);
    }

    @Test
    void updateItem() throws Exception {
        long userId = 100;
        long itemId = 212;
        String name = "ssss";
        String description = "sssaqdewds";
        boolean available = true;
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO(name, description, available);
        String json = objectMapper.writeValueAsString(itemUpdateDTO);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.updateItem(userId, itemId, itemUpdateDTO)).thenReturn(response);

        mockMvc.perform(patch("/items/" + itemId)
                        .contentType("application/json")
                        .content(json)
                        .header(userIdHeader, userId))
                .andExpect(status().isOk());
        verify(itemClient, times(1)).updateItem(userId, itemId, itemUpdateDTO);
        verifyNoMoreInteractions(itemClient);
    }

    @Test
    void getItem() throws Exception {
        long userId = 100;
        long itemId = 212;
        String name = "ssss";
        String description = "sssaqdewds";
        boolean available = true;
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.getItem(userId, itemId)).thenReturn(response);

        mockMvc.perform(get("/items/" + itemId)
                        .contentType("application/json")
                        .header(userIdHeader, userId))
                .andExpect(status().isOk());
        verify(itemClient, times(1)).getItem(userId, itemId);
        verifyNoMoreInteractions(itemClient);
    }

    @Test
    void getAllItems() throws Exception {
        long userId = 100;
        long itemId = 212;
        String name = "ssss";
        String description = "sssaqdewds";
        boolean available = true;
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.getAllItems(userId)).thenReturn(response);

        mockMvc.perform(get("/items")
                        .contentType("application/json")
                        .header(userIdHeader, userId))
                .andExpect(status().isOk());
        verify(itemClient, times(1)).getAllItems(userId);
        verifyNoMoreInteractions(itemClient);
    }

    @Test
    void searchItems() throws Exception {
        long userId = 21321;
        long itemId = 212;
        String name = "ssss";
        String description = "sssaqdewds";
        boolean available = true;
        String text = "sssss";
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(itemClient.searchItems(text)).thenReturn(response);

        mockMvc.perform(get("/items/search?text=sssss")
                        .header(userIdHeader, userId))
                .andExpect(status().isOk());
        verify(itemClient, times(1)).searchItems(text);
        verifyNoMoreInteractions(itemClient);
    }

    @Test
    void addComment() throws Exception {
        long userId = 21321;
        long itemId = 212;
        String text = "saxsdxa";
        CommentCreateDTO comment = new CommentCreateDTO(text);
        String json = objectMapper.writeValueAsString(comment);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.CREATED);

        when(itemClient.addComment(userId, itemId, comment)).thenReturn(response);
        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .contentType("application/json")
                        .content(json)
                        .header(userIdHeader, userId))
                .andExpect(status().isCreated());
        verify(itemClient, times(1)).addComment(userId, itemId, comment);
        verifyNoMoreInteractions(itemClient);
    }
}