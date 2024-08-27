package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(long userId, BookingCreateDto booking);

    BookingDto answerBookingRequest(long userId, long bookingId, boolean isApproved);

    BookingDto getBookingStatus(long userId, long bookingId);

    List<BookingDto> getAllBookingsOfUser(long userId, BookingState state);

    List<BookingDto> getAllBookingsForUserItems(long userId, BookingState state);

}
