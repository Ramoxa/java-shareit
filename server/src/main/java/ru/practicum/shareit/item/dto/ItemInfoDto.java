package ru.practicum.shareit.item.dto;


import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDateInfoDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemInfoDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private Collection<CommentDto> comments;
    private BookingDateInfoDto lastBooking;
    private BookingDateInfoDto nextBooking;
}
