package ru.practicum.shareit.itemTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.comment.dao.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private Item item;
    private ItemDto itemDto;
    private ItemInfoDto itemInfoDto;
    private Comment comment;
    private CommentDto commentDto;
    private CommentRequestDto commentRequestDto;
    private ItemRequest itemRequest;
    private Booking booking;
    private User user;
    private User owner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository, itemRequestRepository);

        user = User.builder().id(1L).name("TestUserName").email("UserEmail@test.com").build();

        owner = User.builder().id(2L).name("TestOwnerName").email("OwnerEmail@test.com").build();

        itemRequest = ItemRequest.builder().id(1L).requestor(user).created(LocalDateTime.now()).description("TestItemRequestDescription").build();

        item = Item.builder().id(1L).name("TestItemName").description("TestItemDescription").request(itemRequest).available(true).owner(owner).bookings(new ArrayList<>()).build();

        booking = Booking.builder().id(1L).start(LocalDateTime.now().plusDays(1)).end(LocalDateTime.now().plusDays(2)).status(BookingStatus.APPROVED).booker(user).item(item).build();

        comment = Comment.builder().id(1L).text("TestCommentText").item(item).author(owner).created(LocalDateTime.now()).build();

        commentDto = CommentMapper.toCommentDto(comment);
        commentRequestDto = new CommentRequestDto(comment.getText());
        itemDto = ItemMapper.toItemDto(item);
        itemInfoDto = ItemMapper.toItemInfoDto(item, BookingMapper.toBookingDateInfoDto(booking), BookingMapper.toBookingDateInfoDto(booking), List.of(CommentMapper.toCommentDto(comment)));

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(any())).thenReturn(item);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong())).thenReturn(List.of(item));
        when(itemRepository.findByNameOrDescriptionContainingIgnoreCase(anyString())).thenReturn(List.of(item));
        when(commentRepository.save(any())).thenReturn(comment);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(anyLong(), anyLong(), any(), any())).thenReturn(List.of(booking));
        when(bookingRepository.findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByStartDesc(anyLong(), anyLong(), any(), any())).thenReturn(Optional.of(booking));
        when(bookingRepository.findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(anyLong(), anyLong(), any(), any())).thenReturn(Optional.of(booking));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));
    }

    @Test
    void create() {
        ItemDto result = itemService.create(user.getId(), itemDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), itemDto.getName());
        Assertions.assertEquals(result.getDescription(), itemDto.getDescription());
        Assertions.assertEquals(result.getRequestId(), itemDto.getRequestId());
        Assertions.assertEquals(result.getAvailable(), itemDto.getAvailable());
        Assertions.assertEquals(result.getId(), itemDto.getId());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void createWithInvalidUserId() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> itemService.create(user.getId(), itemDto), "Expected create() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("User id = " + user.getId() + " not found!"));
    }

    @Test
    void addComment() {
        booking = Booking.builder().id(1L).start(LocalDateTime.now().minusDays(2)).end(LocalDateTime.now().minusDays(1)).status(BookingStatus.APPROVED).booker(user).item(item).build();
        when(bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(anyLong(), anyLong(), any(), any())).thenReturn(List.of(booking));

        CommentDto result = itemService.addComment(booking.getBooker().getId(), booking.getItem().getId(), commentRequestDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getText(), comment.getText());
        Assertions.assertEquals(result.getAuthorName(), owner.getName()); // Изменено на owner
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    void addCommentWithEmptyText() {
        commentRequestDto.setText("");
        ValidationException thrown = assertThrows(ValidationException.class, () -> itemService.addComment(user.getId(), item.getId(), commentRequestDto), "Expected addComment() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("Comment is empty!"));
    }

    @Test
    void addCommentWithoutBooking() {
        when(bookingRepository.findAllByBookerIdAndItemIdAndStatusAndEndBefore(anyLong(), anyLong(), any(), any())).thenReturn(List.of());

        ValidationException thrown = assertThrows(ValidationException.class, () -> itemService.addComment(user.getId(), item.getId(), commentRequestDto), "Expected addComment() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("Access error"));
    }

    @Test
    void updateItemNotOwnedByUser() {
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> itemService.update(user.getId(), item.getId(), itemDto), "Expected update() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("Only the owner can edit an item!"));
    }

    @Test
    void findItemById() {
        ItemInfoDto result = itemService.findItemById(user.getId(), item.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getName(), itemInfoDto.getName());
        Assertions.assertEquals(result.getDescription(), itemInfoDto.getDescription());
        Assertions.assertEquals(result.getRequestId(), itemInfoDto.getRequestId());
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void findItemsByText() {
        List<ItemDto> result = itemService.findItemsByText("text").stream().toList();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get(0).getName(), itemDto.getName());
        Assertions.assertEquals(result.get(0).getDescription(), itemDto.getDescription());
        Assertions.assertEquals(result.get(0).getAvailable(), itemDto.getAvailable());
        Assertions.assertEquals(result.get(0).getRequestId(), itemDto.getRequestId());
        verify(itemRepository, times(1)).findByNameOrDescriptionContainingIgnoreCase(anyString());
    }

    @Test
    void findItemsByEmptyText() {
        List<ItemDto> result = itemService.findItemsByText("").stream().toList();
        Assertions.assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemRepository, times(0)).findByNameOrDescriptionContainingIgnoreCase(anyString());
    }

    @Test
    void validationTest() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> itemService.create(null, itemDto), "Expected create() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("Owner id not specified!"));
    }
}