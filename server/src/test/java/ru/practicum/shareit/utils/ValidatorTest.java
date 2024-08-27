package ru.practicum.shareit.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.ObjectAlreadyExistsException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ItemRepository mockItemRepository;
    @Mock
    private BookingRepository mockBookingRepository;

    @InjectMocks
    Validator validator;

    @Test
    @DisplayName("user exists")
    void validateIfUserNotExistsTest() {
        long userId = 10L;
        when(mockUserRepository.existsById(userId)).thenReturn(true);
        assertDoesNotThrow(() -> validator.validateIfUserNotExists(userId));
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    @DisplayName("user not exists")
    void validateIfUserExistsTest() {
        long userId = 10L;
        when(mockUserRepository.existsById(userId)).thenReturn(false);
        assertThrows(IdNotFoundException.class, () -> validator.validateIfUserNotExists(userId));
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    @DisplayName("email exists")
    void validateIfEmailIsUniqueTest() {
        String email = "anna@mail.ru";
        when(mockUserRepository.hasEmail(email)).thenReturn(true);
        assertThrows(ObjectAlreadyExistsException.class, () -> validator.validateIfEmailIsUnique(email));
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    @DisplayName("email not exists")
    void testValidateIfEmailIsUniqueTest() {
        String email = "anna@mail.ru";
        when(mockUserRepository.hasEmail(email)).thenReturn(false);
        assertDoesNotThrow(() -> validator.validateIfEmailIsUnique(email));
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    @DisplayName("duplicate emails")
    void validateIfEmailIsDuplicateTest() {
        long userId = 10L;
        String email = "anna@mail.ru";
        when(mockUserRepository.hasEmail(userId, email)).thenReturn(true);
        assertThrows(ObjectAlreadyExistsException.class, () -> validator.validateIfEmailIsUnique(email, userId));
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    @DisplayName("not duplicate emails")
    void validateIfEmailIsNotDuplicateTest() {
        long userId = 10L;
        String email = "anna@mail.ru";
        when(mockUserRepository.hasEmail(userId, email)).thenReturn(false);
        assertDoesNotThrow(() -> validator.validateIfEmailIsUnique(email, userId));
        verifyNoMoreInteractions(mockUserRepository);
    }

    @Test
    @DisplayName("user booked item")
    void validateIfUserBookedItemTest() {
        long userId = 121L;
        long itemId = 312L;
        when(mockBookingRepository.isUserBookedItem(userId, itemId)).thenReturn(true);
        assertDoesNotThrow(() -> validator.validateIfUserBookedItem(userId, itemId));
        verifyNoMoreInteractions(mockBookingRepository);
    }

    @Test
    @DisplayName("user did not book item")
    void validateIfUserDidNotBookItemTest() {
        long userId = 121L;
        long itemId = 312L;
        when(mockBookingRepository.isUserBookedItem(userId, itemId)).thenReturn(false);
        assertThrows(InternalServerException.class, () -> validator.validateIfUserBookedItem(userId, itemId));
        verifyNoMoreInteractions(mockBookingRepository);
    }

    @Test
    @DisplayName("user owns item")
    void validateIfUserOwnsItemTest() {
        long userId = 121L;
        long itemId = 312L;
        when(mockItemRepository.existsByIdAndOwner(itemId, userId)).thenReturn(true);
        assertDoesNotThrow(() -> validator.validateIfUserOwnsItem(userId, itemId));
        verifyNoMoreInteractions(mockBookingRepository);
    }

    @Test
    @DisplayName("user does not own item")
    void validateIfUserDoesNotOwnItemTest() {
        long userId = 121L;
        long itemId = 312L;
        when(mockItemRepository.existsByIdAndOwner(itemId, userId)).thenReturn(false);
        assertThrows(IdNotFoundException.class, () -> validator.validateIfUserOwnsItem(userId, itemId));
        verifyNoMoreInteractions(mockBookingRepository);
    }
}