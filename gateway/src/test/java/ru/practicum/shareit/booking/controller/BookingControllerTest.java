package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String userIdHeader = "X-Sharer-User-Id";

    @Test
    void createBooking() throws Exception {
        long userId = 22;
        long itemId = 12312;
        BookingCreateDto bookingCreateDto = new BookingCreateDto(itemId, null, null);
        String json = objectMapper.writeValueAsString(bookingCreateDto);
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.CREATED);

        when(bookingClient.createBooking(userId, bookingCreateDto)).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(json)
                        .header(userIdHeader, userId))
                .andExpect(status().isCreated());
        verify(bookingClient, times(1)).createBooking(userId, bookingCreateDto);
        verifyNoMoreInteractions(bookingClient);
    }

    @Test
    void answerBookingRequest() throws Exception {
        long userId = 12213;
        long bookingId = 1312;
        long itemId = 12312;
        boolean isApproved = true;
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.answerBookingRequest(userId, bookingId, isApproved)).thenReturn(response);

        mockMvc.perform(patch("/bookings/" + bookingId + "?approved=" + isApproved)
                        .header(userIdHeader, userId))
                .andExpect(status().isOk());
        verify(bookingClient, times(1)).answerBookingRequest(userId, bookingId, isApproved);
        verifyNoMoreInteractions(bookingClient);
    }

    @Test
    void getBookingStatus() throws Exception {
        long userId = 12213;
        long bookingId = 1312;
        long itemId = 12312;
        boolean isApproved = true;
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.getBookingStatus(userId, bookingId)).thenReturn(response);

        mockMvc.perform(get("/bookings/" + bookingId)
                        .header(userIdHeader, userId))
                .andExpect(status().isOk());
        verify(bookingClient, times(1)).getBookingStatus(userId, bookingId);
        verifyNoMoreInteractions(bookingClient);
    }

    @Test
    void getAllBookingsByUser() throws Exception {
        long userId = 222;
        BookingState state = BookingState.ALL;
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.getAllBookingsOfUser(userId, state)).thenReturn(response);

        mockMvc.perform(get("/bookings?state=" + state.toString())
                        .header(userIdHeader, userId))
                .andExpect(status().isOk());
        verify(bookingClient, times(1)).getAllBookingsOfUser(userId, state);
        verifyNoMoreInteractions(bookingClient);
    }

    @Test
    void getAllBookingsForUserItems() throws Exception {
        long userId = 222;
        BookingState state = BookingState.ALL;
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(bookingClient.getAllBookingsForUserItems(userId, state)).thenReturn(response);

        mockMvc.perform(get("/bookings/owner?state=" + state.toString())
                        .header(userIdHeader, userId))
                .andExpect(status().isOk());
        verify(bookingClient, times(1)).getAllBookingsForUserItems(userId, state);
        verifyNoMoreInteractions(bookingClient);
    }

}