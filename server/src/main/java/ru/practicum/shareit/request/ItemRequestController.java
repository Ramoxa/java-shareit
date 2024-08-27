package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final String header = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(header) Long userId,
                                 @RequestBody ItemRequestCreateDto itemRequestCreateDto) {
        itemRequestCreateDto.setRequestorId(userId);
        log.info("Creating new item request: {}", itemRequestCreateDto);
        return itemRequestService.create(itemRequestCreateDto);
    }

    @GetMapping
    public List<ItemRequestDto> findAll(@RequestHeader(header) Long userId) {
        log.info("Finding all item requests: {}", userId);
        return itemRequestService.findAllByUserId(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findItemRequestById(@PathVariable Long requestId) {
        log.info("Finding item request by id: {}", requestId);
        return itemRequestService.findItemRequestById(requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllUsersItemRequest(
            @RequestParam(defaultValue = "0", required = false) @Min(0) int from,
            @RequestParam(defaultValue = "10", required = false) @Min(1) int size) {
        log.info("Finding all item requests: {}", size);
        Pageable pageable = PageRequest.of(from, size, Sort.by("created").descending());
        return itemRequestService.findAllUsersItemRequest(pageable);
    }
}