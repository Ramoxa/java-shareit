package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.item.dto.comment.CommentCreateDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.item.ItemCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemDTOWithBookings;
import ru.practicum.shareit.item.dto.item.ItemUpdateDTO;
import ru.practicum.shareit.item.mapper.CommentDTOMapper;
import ru.practicum.shareit.item.mapper.ItemDTOMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.Validator;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;
    private final Validator validator;
    private final ItemDTOMapper itemDTOMapper;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ItemDTO createItem(long userId, ItemCreateDTO itemCreateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException(String.format("User with id=%d does not exists", userId)));
        Long requestId = itemCreateDTO.getRequestId();
        ItemRequest request = null;
        if (requestId != null) {
            request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new IdNotFoundException(
                            String.format("Item request with id=%d does not exists", requestId)));
        }
        Item itemToCreate = itemDTOMapper.fromCreateDTO(user, itemCreateDTO, request);
        Item createdItem = itemRepository.save(itemToCreate);
        log.info("Item {} was created", createdItem);
        return itemDTOMapper.toDTO(createdItem);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ItemDTO updateItem(long userId, long itemId, ItemUpdateDTO itemUpdateDTO) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new IdNotFoundException(String.format("Item with id=%d does not exists", itemId)));
        validator.validateIfUserOwnsItem(userId, itemId);

        Item itemToUpdate = fillInFieldsToUpdate(oldItem, itemUpdateDTO);
        Item updatedItem = itemRepository.save(itemToUpdate);
        log.info("{} was updated", updatedItem);
        return itemDTOMapper.toDTO(itemToUpdate);
    }

    private Item fillInFieldsToUpdate(Item item, ItemUpdateDTO itemUpdateDTO) {
        String name = itemUpdateDTO.getName();
        if (nonNull(name)) {
            item.setName(name);
        }
        String description = itemUpdateDTO.getDescription();
        if (nonNull(description)) {
            item.setDescription(description);
        }
        Boolean available = itemUpdateDTO.getAvailable();
        if (nonNull(available)) {
            item.setAvailable(available);
        }
        return item;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public ItemDTOWithBookings getItem(long userId, long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        return item.map(i -> itemDTOMapper.toDTOWithBookings(userId, i)).orElseThrow(
                () -> new IdNotFoundException("Item with id=" + itemId + " not found"));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public Collection<ItemDTOWithBookings> getAllItems(long userId) {
        validator.validateIfUserNotExists(userId);
        Collection<Item> items = itemRepository.getAllItems(userId);
        return itemDTOMapper.toDTOWithBookings(userId, items);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public Collection<ItemDTO> searchItems(String text) {
        if (isNull(text) || text.isBlank()) {
            return Collections.emptyList();
        }
        Collection<Item> items = itemRepository.searchItems(text);
        return itemDTOMapper.toDTO(items);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public CommentDTO addComment(long userId, long itemId, CommentCreateDTO commentDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IdNotFoundException(String.format("Item with id=%d does not exists", itemId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IdNotFoundException(String.format("User with id=%d does not exists", userId)));
        validator.validateIfUserBookedItem(userId, itemId);
        Comment comment = CommentDTOMapper.fromCreateDTO(user, item, commentDto);
        Comment newComment = commentRepository.save(comment);
        log.info("{} was added", newComment);
        return CommentDTOMapper.toDTO(newComment);
    }
}
