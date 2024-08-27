package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody BookingCreateDto booking) {
        log.info("Received request from user id={} for booking with parameters {}", userId, booking);
        return bookingService.createBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto answerBookingRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable("bookingId") long bookingId,
                                           @RequestParam(name = "approved") Boolean isApproved) {
        log.info("Received request from user id={} to save answer \"{}\" for booking with id={}", userId, isApproved, bookingId);
        return bookingService.answerBookingRequest(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @PathVariable("bookingId") long bookingId) {
        log.info("Received request from user id={} for status of booking with id={}", userId, bookingId);
        return bookingService.getBookingStatus(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(name = "state",
                                                         required = false,
                                                         defaultValue = "ALL") BookingState state) {
        log.info("Received request to get all bookings from user id={} and state {}", userId, state);
        return bookingService.getAllBookingsOfUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForUserItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                                       @RequestParam(name = "state",
                                                               required = false,
                                                               defaultValue = "ALL") BookingState state) {
        log.info("Received request to get all bookings for items of user id={} and state {}", userId, state);
        return bookingService.getAllBookingsForUserItems(userId, state);
    }

}
