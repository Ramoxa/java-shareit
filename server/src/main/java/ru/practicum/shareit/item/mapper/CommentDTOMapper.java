package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.comment.CommentCreateDTO;
import ru.practicum.shareit.item.dto.comment.CommentDTO;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class CommentDTOMapper {
    public static CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment fromCreateDTO(User user, Item item, CommentCreateDTO dto) {
        return Comment.builder()
                .text(dto.getText())
                .item(item)
                .author(user)
                .build();
    }
}
