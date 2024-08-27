package ru.practicum.shareit.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.ObjectAlreadyExistsException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class Validator {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    public void validateIfUserNotExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IdNotFoundException(String.format("User with id=%d does not exists", userId));
        }
    }

    public void validateIfEmailIsUnique(String email) {
        if (userRepository.hasEmail(email)) {
            throw new ObjectAlreadyExistsException("User with email=" + email + " already exists", email);
        }
    }

    public void validateIfEmailIsUnique(String email, long userId) {
        if (userRepository.hasEmail(userId, email)) {
            throw new ObjectAlreadyExistsException("Duplicate email=" + email, email);
        }
    }

    public void validateIfUserOwnsItem(long userId, long itemId) {
        if (!itemRepository.existsByIdAndOwner(itemId, userId)) {
            throw new IdNotFoundException(String.format("User id=%d does not own item id=%d", userId, itemId));
        }
    }

    public void validateIfUserBookedItem(long userId, long itemId) {
        if (!bookingRepository.isUserBookedItem(userId, itemId)) {
            throw new InternalServerException(String.format("User id=%d did not book item id=%d", userId, itemId));
        }
    }

}
