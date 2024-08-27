package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.Collections;
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

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String userIdHeader = "X-Sharer-User-Id";

    @Test
    void createBooking() throws Exception {
        long userId = 22;
        long itemId = 12312;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(2);
        BookingCreateDto bookingCreateDto = new BookingCreateDto(itemId, start, end);
        String json = objectMapper.writeValueAsString(bookingCreateDto);
        ItemDTO itemDTO = new ItemDTO(itemId, "sss", "ssss", true, Collections.emptyList());
        UserDTO userDTO = new UserDTO(userId, "ssss", "sssss");
        BookingDto bookingDto = new BookingDto(itemId, start, end, itemDTO, userDTO, BookingStatus.APPROVED);

        when(bookingService.createBooking(userId, bookingCreateDto)).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(json)
                        .header(userIdHeader, userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(is(itemId), Long.class));
        verify(bookingService, times(1)).createBooking(userId, bookingCreateDto);
        verifyNoMoreInteractions(bookingService);

    }

    @Test
    void answerBookingRequest() throws Exception {
        long userId = 12213;
        long bookingId = 1312;
        long itemId = 12312;
        boolean isApproved = true;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(2);
        ItemDTO itemDTO = new ItemDTO(itemId, "sss", "ssss", true, Collections.emptyList());
        UserDTO userDTO = new UserDTO(userId, "ssss", "sssss");
        BookingDto bookingDto = new BookingDto(itemId, start, end, itemDTO, userDTO, BookingStatus.APPROVED);
        when(bookingService.answerBookingRequest(userId, bookingId, isApproved)).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/" + bookingId + "?approved=" + isApproved)
                        .contentType("application/json")
                        .header(userIdHeader, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(itemId), Long.class));
        verify(bookingService, times(1)).answerBookingRequest(userId, bookingId, isApproved);
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    void getBookingStatus() throws Exception {
        long userId = 12213;
        long bookingId = 1312;
        long itemId = 12312;
        boolean isApproved = true;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(2);
        ItemDTO itemDTO = new ItemDTO(itemId, "sss", "ssss", true, Collections.emptyList());
        UserDTO userDTO = new UserDTO(userId, "ssss", "sssss");
        BookingDto bookingDto = new BookingDto(itemId, start, end, itemDTO, userDTO, BookingStatus.APPROVED);
        when(bookingService.getBookingStatus(userId, bookingId)).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/" + bookingId)
                        .header(userIdHeader, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(itemId), Long.class));
        verify(bookingService, times(1)).getBookingStatus(userId, bookingId);
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    void getAllBookingsByUser() throws Exception {
        long userId = 222;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(2);
        ItemDTO itemDTO = new ItemDTO(12213, "sss", "ssss", true, Collections.emptyList());
        UserDTO userDTO = new UserDTO(222, "ssss", "sssss");
        BookingDto bookingDto1 = new BookingDto(1212, start, end, itemDTO, userDTO, BookingStatus.APPROVED);
        BookingDto bookingDto2 = new BookingDto(21221, start, end, itemDTO, userDTO, BookingStatus.APPROVED);
        List<BookingDto> bookingDtos = List.of(bookingDto1, bookingDto2);
        BookingState state = BookingState.ALL;

        when(bookingService.getAllBookingsOfUser(userId, state)).thenReturn(bookingDtos);

        mockMvc.perform(get("/bookings?state=" + state.toString())
                        .header(userIdHeader, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(is(2L), Long.class));
        verify(bookingService, times(1)).getAllBookingsOfUser(userId, state);
        verifyNoMoreInteractions(bookingService);
    }

    @Test
    void getAllBookingsForUserItems() throws Exception {
        long userId = 222;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(2);
        ItemDTO itemDTO = new ItemDTO(12213, "sss", "ssss", true, Collections.emptyList());
        UserDTO userDTO = new UserDTO(222, "ssss", "sssss");
        BookingDto bookingDto1 = new BookingDto(1212, start, end, itemDTO, userDTO, BookingStatus.APPROVED);
        BookingDto bookingDto2 = new BookingDto(21221, start, end, itemDTO, userDTO, BookingStatus.APPROVED);
        List<BookingDto> bookingDtos = List.of(bookingDto1, bookingDto2);
        BookingState state = BookingState.ALL;

        when(bookingService.getAllBookingsForUserItems(userId, state)).thenReturn(bookingDtos);

        mockMvc.perform(get("/bookings/owner?state=" + state.toString())
                        .header(userIdHeader, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(is(2L), Long.class));
        verify(bookingService, times(1)).getAllBookingsForUserItems(userId, state);
        verifyNoMoreInteractions(bookingService);
    }
}