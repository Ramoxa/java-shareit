package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingState;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                                @RequestBody @Valid BookingCreateDto booking) {
        log.info("Received request from user id={} for booking with parameters {}", userId, booking);
        return bookingClient.createBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> answerBookingRequest(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                                       @PathVariable("bookingId") @Positive long bookingId,
                                                       @RequestParam(name = "approved") boolean isApproved) {
        log.info("Received request from user id={} to save answer \"{}\" for booking with id={}", userId, isApproved, bookingId);
        return bookingClient.answerBookingRequest(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingStatus(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                                   @PathVariable("bookingId") @Positive long bookingId) {
        log.info("Received request from user id={} for status of booking with id={}", userId, bookingId);
        return bookingClient.getBookingStatus(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                                       @RequestParam(name = "state",
                                                               required = false,
                                                               defaultValue = "ALL") BookingState state) {
        log.info("Received request to get all bookings from user id={} and state {}", userId, state);
        return bookingClient.getAllBookingsOfUser(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsForUserItems(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                                             @RequestParam(name = "state",
                                                                     required = false,
                                                                     defaultValue = "ALL") @NotNull BookingState state) {
        log.info("Received request to get all bookings for items of user id={} and state {}", userId, state);
        return bookingClient.getAllBookingsForUserItems(userId, state);
    }
}
