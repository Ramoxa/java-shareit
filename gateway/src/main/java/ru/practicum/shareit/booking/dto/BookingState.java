package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum BookingState {
    /**
     * Represents all bookings.
     */
    ALL,

    /**
     * Represents current bookings.
     */
    CURRENT,

    /**
     * Represents future bookings.
     */
    FUTURE,

    /**
     * Represents past bookings.
     */
    PAST,

    /**
     * Represents rejected bookings.
     */
    REJECTED,

    /**
     * Represents bookings waiting for confirmation.
     */
    WAITING;

    /**
     * Converts a string to a BookingState enum value in a case-insensitive manner.
     *
     * @param stringState the string representation of the BookingState
     * @return an Optional containing the corresponding BookingState if found,
     * otherwise an empty Optional
     */
    public static Optional<BookingState> from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
