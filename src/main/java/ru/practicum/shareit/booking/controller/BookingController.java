package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.InwardBookingDto;
import ru.practicum.shareit.booking.dto.OutwardBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
@Slf4j
@Validated
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity addBooking(@RequestBody @NotNull @Valid InwardBookingDto inwardBookingDto, @RequestHeader(USER_ID_HEADER) @NotNull Long userId) {
        OutwardBookingDto created = bookingService.addBooking(inwardBookingDto, userId);
        log.info("BookingDto created: {}", created.toString());
        return new ResponseEntity(created, HttpStatus.CREATED);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<OutwardBookingDto> approveBooking(@RequestHeader(USER_ID_HEADER) @NotNull Long userId, @PathVariable Long bookingId, @RequestParam Boolean approved) {
        OutwardBookingDto updated = bookingService.approveBooking(bookingId, userId, approved);
        log.info("OutwardBookingDto updated: {}", updated.toString());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<OutwardBookingDto> getBooking(@RequestHeader(USER_ID_HEADER) @NotNull Long userId, @PathVariable Long bookingId) {
        OutwardBookingDto found = bookingService.getBooking(bookingId, userId);
        log.info("OutwardBookingDto found {}: ", found.toString());
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OutwardBookingDto>> getUserBookings(@RequestHeader(USER_ID_HEADER) @NotNull Long userId, @RequestParam(required = false, defaultValue = "ALL") State state) {
        List<OutwardBookingDto> found = bookingService.getUserBookings(userId, state);
        log.info("List<OutwardBookingDto> found: {}", found.toString());
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List> getOwnerBookings(@RequestHeader(USER_ID_HEADER) @NotNull Long userId, @RequestParam(required = false) String state) {
        State queryState = (state == null) ? State.ALL : State.valueOf(state.toUpperCase());
        List found = bookingService.getOwnerBookings(userId, queryState);
        log.info("List found: " + found.toString());
        return new ResponseEntity<>(found, HttpStatus.OK);
    }
}
