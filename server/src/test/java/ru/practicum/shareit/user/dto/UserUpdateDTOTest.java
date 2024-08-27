package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserUpdateDTOTest {
    @Test
    void getterAndSetterTest() {
        String name = "Anna";
        String email = "anna@mail.ru";

        UserCreateDTO dto = new UserCreateDTO();
        dto.setName(name);
        dto.setEmail(email);

        assertEquals(name, dto.getName());
        assertEquals(email, dto.getEmail());
    }

    @Test
    void equalsAndHashCodeTest() {
        String name = "Anna";
        String email = "anna@mail.ru";

        UserCreateDTO dto1 = new UserCreateDTO(name, email);
        UserCreateDTO dto2 = new UserCreateDTO(name, email);
        UserCreateDTO dto3 = new UserCreateDTO(name, "other email");
        UserCreateDTO dto4 = new UserCreateDTO("other name", email);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1, dto4);
    }

}