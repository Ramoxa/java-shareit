package ru.practicum.shareit.item.dto.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.comment.CommentDTO;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemDTO {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private Collection<CommentDTO> comments;
}
