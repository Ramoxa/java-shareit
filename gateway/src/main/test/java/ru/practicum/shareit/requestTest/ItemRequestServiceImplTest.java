package ru.practicum.shareit.requestTest;

import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {

    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private ItemRequestCreateDto itemRequestCreateDto;
    private Pageable pageable;
    private ItemRequestService itemRequestService;
    private ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemRequestService = new ItemRequestServiceImpl(
                itemRequestRepository,
                userRepository);

        user = User.builder()
                .id(1L)
                .name("TestUserName")
                .email("TestUserEmail@test.com")
                .build();
        userRepository.save(user);

        itemRequest = ItemRequest.builder()
                .id(1L)
                .requestor(user)
                .created(LocalDateTime.now())
                .description("TestItemRequestDescription")
                .build();

        itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestCreateDto = new ItemRequestCreateDto(user.getId(), "TestItemRequestText");

        pageable = PageRequest.of(0, 10, Sort.by("created").descending());
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), List.of(itemRequest).size());
        final Page<ItemRequest> page = new PageImpl<>(List.of(itemRequest).subList(start, end), pageable, List.of(itemRequest).size());
        when(itemRequestRepository.findAll((Pageable) any())).thenReturn(page);

        when(itemRequestRepository.save(any())).thenReturn(itemRequest);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRequestRepository.findAllByRequestorId(any())).thenReturn(List.of(itemRequest));
    }

    @Test
    void findAllByUserId() {
        List<ItemRequestDto> result = itemRequestService.findAllByUserId(user.getId());
        validateList(result);
        verify(itemRequestRepository, times(1)).findAllByRequestorId(anyLong());
    }

    @Test
    void create() {
        ItemRequestDto result = itemRequestService.create(itemRequestCreateDto);
        validate(result);
        verify(itemRequestRepository, times(1)).save(any());
    }

    @Test
    void findItemRequestById() {
        ItemRequestDto result = itemRequestService.findItemRequestById(itemRequest.getRequestor().getId());
        validate(result);
    }

    @Test
    void findAllUsersItemRequest() {
        List<ItemRequestDto> result = itemRequestService.findAllUsersItemRequest(pageable);
        validateList(result);
        verify(itemRequestRepository, times(1)).findAll((Pageable) any());
    }

    void validate(ItemRequestDto result) {
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCreated(), itemRequestDto.getCreated());
        Assertions.assertEquals(result.getDescription(), itemRequestDto.getDescription());
        Assertions.assertEquals(result.getRequestor(), itemRequestDto.getRequestor());
        Assertions.assertEquals(result.getId(), itemRequestDto.getId());
    }

    void validateList(List<ItemRequestDto> result) {
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getFirst().getCreated(), itemRequestDto.getCreated());
        Assertions.assertEquals(result.getFirst().getDescription(), itemRequestDto.getDescription());
        Assertions.assertEquals(result.getFirst().getRequestor(), itemRequestDto.getRequestor());
        Assertions.assertEquals(result.getFirst().getId(), itemRequestDto.getId());
    }
}