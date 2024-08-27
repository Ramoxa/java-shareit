package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class UserCreateDTO {
    @NotBlank(message = "name can not be empty")
    @Length(min = 1, max = 50, message = "name must have 1-50 digits")
    private String name;
    @NotNull(message = "email is mandatory")
    @Email(message = "incorrect email")
    private String email;
}
