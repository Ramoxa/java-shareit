package ru.practicum.shareit.bookingTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTests {

    private BookingService bookingService;
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private Booking booking;
    private BookingDto bookingDto;
    private BookingRequestDto bookingRequestDto;
    private User user;
    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        userRepository = mock(UserRepository.class);
        itemRepository = mock(ItemRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        user = User.builder()
                .id(1L)
                .name("TestUserName")
                .email("TestUserEmail@test.com")
                .build();

        owner = User.builder()
                .id(2L)
                .name("TestOwnerName")
                .email("TestOwnerEmail@test.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("TestItemName")
                .description("TestItemDescription")
                .request(null)
                .available(true)
                .owner(owner)
                .build();

        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .booker(user)
                .item(item)
                .build();

        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(item.getId());
        bookingRequestDto.setStart(booking.getStart());
        bookingRequestDto.setEnd(booking.getEnd());
        bookingDto = BookingMapper.toBookingDto(booking);

        when(bookingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong())).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(anyLong())).thenReturn(List.of(booking));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    }

    @Test
    void create() {
        final BookingDto result = bookingService.createBooking(user.getId(), bookingRequestDto);
        validate(result);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void createThrowsItemNotFoundException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.createBooking(user.getId(), bookingRequestDto));
        verify(bookingRepository, times(0)).save(any());
    }

    @Test
    void createThrowsEndDateBeforeNowException() {
        bookingRequestDto.setEnd(LocalDateTime.now().minusDays(1));
        Assertions.assertThrows(ValidationException.class, () -> bookingService.createBooking(user.getId(), bookingRequestDto));
        verify(bookingRepository, times(0)).save(any());
    }

    @Test
    void createThrowsUserIsOwnerException() {
        item.setOwner(user);
        bookingRequestDto.setItemId(item.getId());
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.createBooking(user.getId(), bookingRequestDto));
        verify(bookingRepository, times(0)).save(any());
    }

    @Test
    void createThrowsItemsNotAvailableException() {
        item.setAvailable(false);
        Assertions.assertThrows(ValidationException.class, () -> bookingService.createBooking(user.getId(), bookingRequestDto));
        verify(bookingRepository, times(0)).save(any());
    }

    @Test
    void setApproved() {
        BookingDto result = bookingService.approvedBooking(owner.getId(), booking.getId(), true);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStart(), bookingDto.getStart());
        Assertions.assertEquals(result.getEnd(), bookingDto.getEnd());
        Assertions.assertEquals(result.getItem(), bookingDto.getItem());
        Assertions.assertEquals(result.getStatus(), BookingStatus.APPROVED.toString());
        Assertions.assertEquals(result.getBooker(), bookingDto.getBooker());
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void setApprovedThrowsUserIsNotOwnerException() {
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.approvedBooking(user.getId(), booking.getId(), true));
    }

    @Test
    void setApprovedThrowsBookingAlreadyApprovedException() {
        booking.setStatus(BookingStatus.APPROVED);
        Assertions.assertThrows(ValidationException.class, () -> bookingService.approvedBooking(owner.getId(), booking.getId(), true));
    }

    @Test
    void findById() {
        BookingDto result = bookingService.getBookingById(booking.getId(), user.getId());
        validate(result);
        verify(bookingRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByIdThrowsNotFoundException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booking.getId(), user.getId()));
    }

    @Test
    void findAllByBookerAndStatus() {
        List<BookingDto> result = bookingService.findAllByBookerAndStatus(user.getId(), "ALL").stream().toList();
        validateList(result);
        verify(bookingRepository, times(1)).findAllByBookerIdOrderByStartDesc(anyLong());
    }

    @Test
    void findAllByOwnerAndStatus() {
        List<BookingDto> result = bookingService.findAllByOwnerAndStatus(owner.getId(), "ALL").stream().toList();
        validateList(result);
        verify(bookingRepository, times(1)).findAllByItemOwnerIdOrderByStartDesc(anyLong());
    }

    void validate(BookingDto result) {
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getStart(), bookingDto.getStart());
        Assertions.assertEquals(result.getEnd(), bookingDto.getEnd());
        Assertions.assertEquals(result.getItem(), bookingDto.getItem());
        Assertions.assertEquals(result.getStatus(), bookingDto.getStatus());
        Assertions.assertEquals(result.getBooker(), bookingDto.getBooker());
    }

    void validateList(List<BookingDto> result) {
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get(0).getStart(), bookingDto.getStart());
        Assertions.assertEquals(result.get(0).getEnd(), bookingDto.getEnd());
        Assertions.assertEquals(result.get(0).getItem(), bookingDto.getItem());
        Assertions.assertEquals(result.get(0).getStatus(), bookingDto.getStatus());
        Assertions.assertEquals(result.get(0).getBooker(), bookingDto.getBooker());
    }
}