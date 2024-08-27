package ru.practicum.shareit.request.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemRequestCreateDto {
    private Long requestorId;
    private String description;
}