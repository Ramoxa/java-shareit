package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.item.ItemDTOForRequest;
import ru.practicum.shareit.request.dto.ItemRequestCreateDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTOWithAnswers;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService mockItemRequestService;

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
        LocalDateTime created = LocalDateTime.now();
        ItemRequestCreateDTO itemRequestCreateDTO = new ItemRequestCreateDTO(description);
        String json = objectMapper.writeValueAsString(itemRequestCreateDTO);
        ItemRequestDTO itemRequestDTO = new ItemRequestDTO(userId, description, created);

        when(mockItemRequestService.createItemRequest(userId, itemRequestCreateDTO)).thenReturn(itemRequestDTO);

        mockMvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(json)
                        .header(userIdHeader, userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(is(userId), Long.class))
                .andExpect(jsonPath("$.description").value(is(description), String.class));
        verify(mockItemRequestService, times(1)).createItemRequest(userId, itemRequestCreateDTO);
        verifyNoMoreInteractions(mockItemRequestService);
    }

    @Test
    @DisplayName("get requests from user")
    void getUserRequests() throws Exception {
        long userId = 1000L;

        long id1 = 1L;
        String desc1 = "bla-bla1";
        LocalDateTime cr1 = LocalDateTime.of(2024, 1, 1, 1, 1);
        List<ItemDTOForRequest> items1 = List.of(new ItemDTOForRequest(100L, "Anna1", 10L));
        ItemRequestDTOWithAnswers irdwa1 = new ItemRequestDTOWithAnswers(id1, desc1, cr1, items1);

        long id2 = 2L;
        String desc2 = "bla-bla2";
        LocalDateTime cr2 = LocalDateTime.of(2024, 2, 2, 2, 2);
        List<ItemDTOForRequest> items2 = List.of(new ItemDTOForRequest(200L, "Anna2", 20L));
        ItemRequestDTOWithAnswers irdwa2 = new ItemRequestDTOWithAnswers(id2, desc2, cr2, items2);

        long id3 = 3L;
        String desc3 = "bla-bla3";
        LocalDateTime cr3 = LocalDateTime.of(2024, 3, 3, 3, 3);
        List<ItemDTOForRequest> items3 = List.of(new ItemDTOForRequest(300L, "Anna3", 30L));
        ItemRequestDTOWithAnswers irdwa3 = new ItemRequestDTOWithAnswers(id3, desc3, cr3, items3);

        List<ItemRequestDTOWithAnswers> answers = List.of(irdwa1, irdwa2, irdwa3);

        when(mockItemRequestService.getUserRequests(userId)).thenReturn(answers);

        mockMvc.perform(get("/requests").header(userIdHeader, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(is(answers.size())));
        verify(mockItemRequestService, times(1)).getUserRequests(userId);
        verifyNoMoreInteractions(mockItemRequestService);
    }

    @Test
    @DisplayName("get all requests")
    void getAllRequestsExceptUser() throws Exception {
        long userId = 1000L;
        ItemRequestDTO ird1 = new ItemRequestDTO(1L, "desc1", LocalDateTime.now());
        ItemRequestDTO ird2 = new ItemRequestDTO(2L, "desc2", LocalDateTime.now());
        ItemRequestDTO ird3 = new ItemRequestDTO(3L, "desc3", LocalDateTime.now());
        List<ItemRequestDTO> answers = List.of(ird1, ird2, ird3);
        when(mockItemRequestService.getAllRequestsExceptUser(userId)).thenReturn(answers);

        mockMvc.perform(get("/requests/all").header(userIdHeader, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(is(answers.size())));
        verify(mockItemRequestService, times(1)).getAllRequestsExceptUser(userId);
        verifyNoMoreInteractions(mockItemRequestService);
    }

    @Test
    void getRequestById() throws Exception {
        long userId = 1000L;
        long requestId = 1000L;

        String desc1 = "bla-bla1";
        LocalDateTime cr1 = LocalDateTime.of(2024, 1, 1, 1, 1);
        List<ItemDTOForRequest> items1 = List.of(new ItemDTOForRequest(100L, "Anna1", 10L));
        ItemRequestDTOWithAnswers irdwa1 = new ItemRequestDTOWithAnswers(requestId, desc1, cr1, items1);
        when(mockItemRequestService.getRequestById(requestId)).thenReturn(irdwa1);

        mockMvc.perform(get("/requests/" + requestId).header(userIdHeader, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(requestId), Long.class))
                .andExpect(jsonPath("$.description").value(is(desc1), String.class));

        verify(mockItemRequestService, times(1)).getRequestById(requestId);
        verifyNoMoreInteractions(mockItemRequestService);
    }
}