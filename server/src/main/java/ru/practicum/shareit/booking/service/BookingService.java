package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.Collection;

public interface BookingService {
    BookingDto createBooking(Long ownerId, BookingRequestDto bookingRequestDto);

    BookingDto approvedBooking(Long userId, Long bookingId, Boolean isApproved);

    BookingDto getBookingById(Long userId, Long bookingId);

    Collection<BookingDto> findAllByBookerAndStatus(Long userId, String state);

    Collection<BookingDto> findAllByOwnerAndStatus(Long userId, String state);
}