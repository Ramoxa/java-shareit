package ru.practicum.shareit.itemTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.service.ItemService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    private static final String header = "X-Sharer-User-Id";

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    public CommentDto getCommentDto() {
        return CommentDto.builder()
                .id(1L)
                .text("TestCommentText")
                .authorName("TestAuthorName")
                .created(LocalDateTime.now())
                .build();
    }

    public ItemInfoDto getItemInfoDto() {
        return ItemInfoDto.builder()
                .id(1L)
                .name("TestItemName")
                .description("TestItemDescription")
                .comments(List.of(getCommentDto()))
                .build();
    }

    public ItemDto getTestItemDto() {
        return ItemDto.builder()
                .id(1L)
                .name("TestItemName")
                .description("TestItemDescription")
                .requestId(1L)
                .available(true)
                .build();
    }

    @Test
    void findAll() throws Exception {
        when(itemService.findItemsByUserId(anyLong())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/items")
                        .header(header, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1)).findItemsByUserId(anyLong());
    }

    @Test
    void getItemDto() throws Exception {
        ItemInfoDto itemInfoDto = getItemInfoDto();
        when(itemService.findItemById(anyLong(), anyLong())).thenReturn(itemInfoDto);
        mockMvc.perform(get("/items/1")
                        .header(header, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemInfoDto.getName())))
                .andExpect(jsonPath("$.description", is(itemInfoDto.getDescription())));
        verify(itemService, times(1)).findItemById(anyLong(), anyLong());
    }

    @Test
    void create() throws Exception {
        ItemDto itemDto = getTestItemDto();
        when(itemService.create(anyLong(), any())).thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .header(header, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
        verify(itemService, times(1)).create(anyLong(), any());
    }

    @Test
    void update() throws Exception {
        ItemDto itemDto = getTestItemDto();
        when(itemService.update(anyLong(), anyLong(), any())).thenReturn(itemDto);
        mockMvc.perform(patch("/items/1")
                        .header(header, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable()), Boolean.class));
        verify(itemService, times(1)).update(anyLong(), anyLong(), any());
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = getCommentDto();
        when(itemService.addComment(anyLong(), anyLong(), any())).thenReturn(commentDto);
        mockMvc.perform(post("/items/1/comment")
                        .header(header, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))));
        verify(itemService, times(1)).addComment(anyLong(), anyLong(), any());
    }

    @Test
    void searchItemDto() throws Exception {
        when(itemService.findItemsByText(anyString())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/items/search?text=test")
                        .header(header, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemService, times(1)).findItemsByText(anyString());
    }
}