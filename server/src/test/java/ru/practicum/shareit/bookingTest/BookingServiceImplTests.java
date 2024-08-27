package ru.practicum.shareit.bookingTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
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
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private Booking booking;
    private BookingDto bookingDto;
    private BookingRequestDto bookingRequestDto;
    private User user;
    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

        user = User.builder()
                .id(1L)
                .name("TestUserName")
                .email("TestUserEmail@test.com")
                .build();
        userRepository.save(user);

        owner = User.builder()
                .id(2L)
                .name("TestOwnerName")
                .email("TestOwnerEmail@test.com")
                .build();
        userRepository.save(owner);

        item = Item.builder()
                .id(1L)
                .name("TestItemName")
                .description("TestItemDescription")
                .request(null)
                .available(true)
                .owner(owner)
                .build();
        itemRepository.save(item);

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
    }

    @Test
    void create() {
        final BookingDto result = bookingService.createBooking(user.getId(), bookingRequestDto);
        validate(result);
        verify(bookingRepository, times(1)).save(any());
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
    void findById() {
        BookingDto result = bookingService.getBookingById(booking.getId(), user.getId());
        validate(result);
        verify(bookingRepository, times(1)).findById(anyLong());
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
        Assertions.assertEquals(result.getFirst().getStart(), bookingDto.getStart());
        Assertions.assertEquals(result.getFirst().getEnd(), bookingDto.getEnd());
        Assertions.assertEquals(result.getFirst().getItem(), bookingDto.getItem());
        Assertions.assertEquals(result.getFirst().getStatus(), bookingDto.getStatus());
        Assertions.assertEquals(result.getFirst().getBooker(), bookingDto.getBooker());
    }
}