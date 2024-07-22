package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private final int id;
    @NotBlank
    private final String description;
    private final User requestor;
    private final LocalDateTime created;

}