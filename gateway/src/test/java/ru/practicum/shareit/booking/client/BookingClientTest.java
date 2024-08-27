package ru.practicum.shareit.booking.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookingClientTest {

    @Mock
    RestTemplate restTemplate;

    private final BookingClient bookingClient =
            new BookingClient("http://localhost:9090", new RestTemplateBuilder());

    @Test
    void createBooking() {
        long userId = 22;
        long itemId = 12312;
        BookingCreateDto bookingCreateDto = new BookingCreateDto(itemId, null, null);
        String path = "";
        assertThrows(Throwable.class, () -> bookingClient.createBooking(userId, bookingCreateDto));
        assertThrows(Throwable.class, () -> bookingClient.post(path, userId, null, bookingCreateDto));
    }

    @Test
    void answerBookingRequest() {
        long userId = 22;
        long bookingId = 12312;
        boolean isApproved = true;
        String path = String.format("/%d", bookingId);
        Map<String, String> query = Map.of("approved", Boolean.TRUE.toString());
        assertThrows(Throwable.class, () -> bookingClient.answerBookingRequest(userId, bookingId, isApproved));
        assertThrows(Throwable.class, () -> bookingClient.patch(path, userId, query, null));

    }

    @Test
    void getBookingStatus() {
        long userId = 22;
        long bookingId = 12312;
        String path = String.format("/%d", bookingId);
        assertThrows(Throwable.class, () -> bookingClient.getBookingStatus(userId, bookingId));
        assertThrows(Throwable.class, () -> bookingClient.get(path, userId, null));
    }

    @Test
    void getAllBookingsOfUser() {
        long userId = 22;
        BookingState state = BookingState.ALL;
        String path = "";
        Map<String, String> query = Map.of("state", state.name());
        assertThrows(Throwable.class, () -> bookingClient.getAllBookingsOfUser(userId, state));
        assertThrows(Throwable.class, () -> bookingClient.get(path, userId, query));
    }

    @Test
    void getAllBookingsForUserItems() {
        String path = "/owner";
        long userId = 22;
        BookingState state = BookingState.ALL;
        Map<String, String> query = Map.of("state", state.name());
        assertThrows(Throwable.class, () -> bookingClient.getAllBookingsForUserItems(userId, state));
        assertThrows(Throwable.class, () -> bookingClient.get(path, userId, query));
    }
}