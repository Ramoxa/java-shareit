package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final String header = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(header) Long userId,
                             @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Create booking request: {}", bookingRequestDto);
        return bookingService.createBooking(userId, bookingRequestDto);
    }

    @GetMapping
    public Collection<BookingDto> findAll(
            @RequestHeader(header) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("Find all booking requests: {}", state);
        return bookingService.findAllByBookerAndStatus(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllByOwnerAndStatus(
            @RequestHeader(header) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("Find all booking by owner requests: {}", state);
        return bookingService.findAllByOwnerAndStatus(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setApproved(@RequestHeader(header) Long userId,
                                  @PathVariable Long bookingId,
                                  @RequestParam Boolean approved) {
        log.info("Set approved booking request: {}", bookingId);
        return bookingService.approvedBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader(header) Long userId,
                               @PathVariable Long bookingId) {
        log.info("Find booking by id request: {}", bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }
}
