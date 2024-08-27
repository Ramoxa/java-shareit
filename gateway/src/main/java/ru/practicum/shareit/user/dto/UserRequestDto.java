package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserRequestDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}