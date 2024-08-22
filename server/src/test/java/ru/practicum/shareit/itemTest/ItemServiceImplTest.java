package ru.practicum.shareit.itemTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.dao.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.model.Comment;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    private ItemService itemService;
    private ItemRepository itemRepository;
    private CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private Item item;
    private ItemDto itemDto;
    private ItemInfoDto itemInfoDto;
    private Comment comment;
    private CommentDto commentDto;
    private CommentRequestDto commentRequestDto;
    private ItemRequest itemRequest;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private Booking booking;
    private User user;
    private User owner;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository);

        user = User.builder()
                .id(1L)
                .name("TestUserName")
                .email("UserEmail@test.com")
                .build();
        userRepository.save(user);

        owner = User.builder()
                .id(2L)
                .name("TestOwnerName")
                .email("OwnerEmail@test.com")
                .build();
        userRepository.save(owner);

        item = Item.builder()
                .id(1L)
                .name("TestItemName")
                .description("TestItemDescription")
                .request(itemRequest)
                .available(true)
                .owner(owner)
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .requestor(user)
                .created(LocalDateTime.now())
                .description("TestItemRequestDescription")
                .build();
        itemRequestRepository.save(itemRequest);

        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .booker(user)
                .item(item)
                .build();
        bookingRepository.save(booking);

        comment = Comment.builder()
                .id(1L)
                .text("TestCommentText")
                .item(item)
                .author(owner)
                .created(LocalDateTime.now())
                .build();

        commentDto = CommentMapper.toCommentDto(comment);
        commentRequestDto = new CommentRequestDto(comment.getText());
        itemDto = ItemMapper.toItemDto(item);
        itemInfoDto = ItemMapper.toItemInfoDto(item,
                BookingMapper.toBookingDateInfoDto(booking),
                BookingMapper.toBookingDateInfoDto(booking),
                List.of(CommentMapper.toCommentDto(comment))
        );

        when(itemRepository.save(any())).thenReturn(item);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong())).thenReturn(List.of(item));
        when(itemRepository.findByNameOrDescriptionContainingIgnoreCase(anyString())).thenReturn(List.of(item));
        when(commentRepository.save(any())).thenReturn(comment);
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
    void addComment() {
        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().minusDays(2))
                .status(BookingStatus.APPROVED)
                .booker(user)
                .item(item)
                .build();
        bookingRepository.save(booking);

        CommentDto result = itemService.addComment(booking.getBooker().getId(), booking.getItem().getId(), commentRequestDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), commentDto.getId());
        Assertions.assertEquals(result.getText(), commentDto.getText());
        Assertions.assertEquals(result.getAuthorName(), commentDto.getAuthorName());
        Assertions.assertEquals(result.getCreated(), commentDto.getCreated());
        verify(commentRepository, times(1)).save(any());
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
        Assertions.assertEquals(result.getFirst().getName(), itemDto.getName());
        Assertions.assertEquals(result.getFirst().getDescription(), itemDto.getDescription());
        Assertions.assertEquals(result.getFirst().getAvailable(), itemDto.getAvailable());
        Assertions.assertEquals(result.getFirst().getRequestId(), itemDto.getRequestId());
        verify(itemRepository, times(1)).findByNameOrDescriptionContainingIgnoreCase(anyString());
    }
}