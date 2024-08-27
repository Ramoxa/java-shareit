package ru.practicum.shareit.item.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
@EqualsAndHashCode
public class ItemCreateDTO {
    @NotBlank(message = "name is empty")
    @Length(max = 50, message = "name must be less than 50 digits")
    private String name;

    @NotBlank(message = "name is empty")
    @Length(max = 200, message = "description must be less than 50 digits")
    private String description;

    @NotNull(message = "item availability is null")
    private Boolean available;

    private Long requestId;
}
