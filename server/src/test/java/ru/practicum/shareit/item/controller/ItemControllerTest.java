package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.comment.CommentCreateDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.item.ItemCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemDTOWithBookings;
import ru.practicum.shareit.item.dto.item.ItemUpdateDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

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
        ItemDTO itemDTO = new ItemDTO(id, name, description, available,
                List.of(new CommentDTO(1, "ss", "ss", LocalDateTime.now())));
        when(itemService.createItem(userId, itemCreateDTO)).thenReturn(itemDTO);

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(json)
                        .header(userIdHeader, userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(is(id), Long.class));
        verify(itemService, times(1)).createItem(userId, itemCreateDTO);
        verifyNoMoreInteractions(itemService);
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
        ItemDTO itemDTO = new ItemDTO(itemId, name, description, available,
                List.of(new CommentDTO(1, "ss", "ss", LocalDateTime.now())));
        when(itemService.updateItem(userId, itemId, itemUpdateDTO)).thenReturn(itemDTO);

        mockMvc.perform(patch("/items/" + itemId)
                        .contentType("application/json")
                        .content(json)
                        .header(userIdHeader, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(itemId), Long.class));
        verify(itemService, times(1)).updateItem(userId, itemId, itemUpdateDTO);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    void getItem() throws Exception {
        long userId = 100;
        long itemId = 212;
        String name = "ssss";
        String description = "sssaqdewds";
        boolean available = true;
        ItemDTOWithBookings itemDTO = new ItemDTOWithBookings(itemId, name, description, available,
                List.of(new CommentDTO(1, "ss", "ss", LocalDateTime.now())), LocalDateTime.now(),
                LocalDateTime.now());
        when(itemService.getItem(userId, itemId)).thenReturn(itemDTO);

        mockMvc.perform(get("/items/" + itemId)
                        .contentType("application/json")
                        .header(userIdHeader, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(itemId), Long.class));
        verify(itemService, times(1)).getItem(userId, itemId);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    void getAllItems() throws Exception {
        long userId = 100;
        long itemId = 212;
        String name = "ssss";
        String description = "sssaqdewds";
        boolean available = true;
        ItemDTOWithBookings itemDTO = new ItemDTOWithBookings(itemId, name, description, available,
                List.of(new CommentDTO(1, "ss", "ss", LocalDateTime.now())), LocalDateTime.now(),
                LocalDateTime.now());
        Collection<ItemDTOWithBookings> collection = List.of(itemDTO);

        when(itemService.getAllItems(userId)).thenReturn(collection);

        mockMvc.perform(get("/items")
                        .contentType("application/json")
                        .header(userIdHeader, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(itemService, times(1)).getAllItems(userId);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    void searchItems() throws Exception {
        long userId = 21321;
        long itemId = 212;
        String name = "ssss";
        String description = "sssaqdewds";
        boolean available = true;
        ItemDTO itemDTO = new ItemDTO(itemId, name, description, available,
                List.of(new CommentDTO(1, "ss", "ss", LocalDateTime.now())));
        Collection<ItemDTO> collection = List.of(itemDTO);
        String text = "sssss";

        when(itemService.searchItems(text)).thenReturn(collection);

        mockMvc.perform(get("/items/search?text=sssss")
                        .header(userIdHeader, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(itemService, times(1)).searchItems(text);
        verifyNoMoreInteractions(itemService);
    }

    @Test
    void addComment() throws Exception {
        long userId = 21321;
        long itemId = 212;
        String text = "saxsdxa";
        CommentCreateDTO comment = new CommentCreateDTO(text);
        String json = objectMapper.writeValueAsString(comment);
        CommentDTO commentDTO = new CommentDTO(12, text, "ssadsa", LocalDateTime.now());
        when(itemService.addComment(userId, itemId, comment)).thenReturn(commentDTO);
        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .contentType("application/json")
                        .content(json)
                        .header(userIdHeader, userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(12));
        verify(itemService, times(1)).addComment(userId, itemId, comment);
        verifyNoMoreInteractions(itemService);
    }
}