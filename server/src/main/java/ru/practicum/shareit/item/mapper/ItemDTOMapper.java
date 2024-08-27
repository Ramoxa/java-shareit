package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.dto.item.ItemCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemDTO;
import ru.practicum.shareit.item.dto.item.ItemDTOForRequest;
import ru.practicum.shareit.item.dto.item.ItemDTOWithBookings;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemDTOMapper {
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public Item fromCreateDTO(User user, ItemCreateDTO itemCreateDTO, ItemRequest request) {
        return Item.builder()
                .name(itemCreateDTO.getName())
                .description(itemCreateDTO.getDescription())
                .available(itemCreateDTO.getAvailable())
                .owner(user)
                .request(request)
                .build();
    }

    private ItemDTO toSemiFinishedDTO(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public ItemDTO toDTO(Item item) {
        ItemDTO itemDTO = toSemiFinishedDTO(item);
        List<CommentDTO> commentsDTO = commentRepository
                .getCommentsByItem(item.getId())
                .stream()
                .map(CommentDTOMapper::toDTO)
                .toList();
        itemDTO.setComments(commentsDTO);
        return itemDTO;
    }

    public Collection<ItemDTO> toDTO(Collection<Item> items) {
        Map<Long, List<CommentDTO>> commentMap = getCommentsMapByItemIds(items);
        return items.stream()
                .map(this::toSemiFinishedDTO)
                .peek(itemDTO -> itemDTO.setComments(commentMap.get(itemDTO.getId())))
                .toList();
    }

    private Map<Long, List<CommentDTO>> getCommentsMapByItemIds(Collection<Item> items) {
        Collection<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();
        Collection<Comment> allComments = commentRepository.getCommentsByItemIds(itemIds);
        Map<Long, List<CommentDTO>> map = new HashMap<>();
        for (Comment comment : allComments) {
            Long itemId = comment.getItem().getId();
            if (!map.containsKey(itemId)) {
                map.put(itemId, new ArrayList<>());
            }
            map.get(itemId).add(CommentDTOMapper.toDTO(comment));
        }
        return map;
    }

    private ItemDTOWithBookings toSemiFinishedDTOWithBookings(Item item) {
        return ItemDTOWithBookings.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public ItemDTOWithBookings toDTOWithBookings(long userId, Item item) {
        ItemDTOWithBookings itemDTO = toSemiFinishedDTOWithBookings(item);
        List<CommentDTO> commentsDTO = commentRepository
                .getCommentsByItem(item.getId())
                .stream()
                .map(CommentDTOMapper::toDTO)
                .toList();
        itemDTO.setComments(commentsDTO);
        var lastBooking = bookingRepository.getLastBookingForItemOwnedByUser(userId, item.getId());
        itemDTO.setLastBooking(lastBooking.map(Booking::getEnd).orElse(null));
        var nextBooking = bookingRepository.getNextBookingForItemOwnedByUser(userId, item.getId());
        itemDTO.setNextBooking(nextBooking.map(Booking::getStart).orElse(null));
        return itemDTO;
    }

    public Collection<ItemDTOWithBookings> toDTOWithBookings(long userId, Collection<Item> items) {
        Map<Long, List<CommentDTO>> commentMap = getCommentsMapByItemIds(items);
        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();
        Map<Long, Booking> lastBookingMap = getLastBookingMap(userId, itemIds);
        Map<Long, Booking> nextBookingMap = getNextBookingMap(userId, itemIds);
        return items.stream()
                .map(this::toSemiFinishedDTOWithBookings)
                .peek(dto -> dto.setComments(commentMap.get(dto.getId())))
                .peek(dto -> {
                    Booking booking = lastBookingMap.get(dto.getId());
                    LocalDateTime date = (booking == null) ? null : booking.getEnd();
                    dto.setLastBooking(date);
                })
                .peek(dto -> {
                    Booking booking = nextBookingMap.get(dto.getId());
                    LocalDateTime date = (booking == null) ? null : booking.getStart();
                    dto.setNextBooking(date);
                })
                .toList();
    }

    private Map<Long, Booking> getLastBookingMap(long userId, Collection<Long> itemIds) {
        Collection<Booking> allBookings = bookingRepository.getAllLastBookings(userId, itemIds);
        return allBookings.stream()
                .collect(Collectors.toMap(b -> b.getItem().getId(), Function.identity()));
    }

    private Map<Long, Booking> getNextBookingMap(long userId, Collection<Long> itemIds) {
        Collection<Booking> allBookings = bookingRepository.getAllNextBooking(userId, itemIds);
        return allBookings.stream()
                .collect(Collectors.toMap(b -> b.getItem().getId(), Function.identity()));
    }

    public static ItemDTOForRequest toDTOForRequest(Item item) {
        return ItemDTOForRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }

}