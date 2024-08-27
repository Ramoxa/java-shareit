package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDTOTest {
    private final JacksonTester<UserDTO> json;

    @Test
    void jsonTest() throws IOException {
        long id = 1212L;
        String name = "Anna";
        String email = "anna@mail.ru";
        UserDTO dto = new UserDTO(id, name, email);

        JsonContent<UserDTO> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) id);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(name);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(email);
    }

    @Test
    void getterAndSetterTest() {
        long id = 1212L;
        String name = "Anna";
        String email = "anna@mail.ru";

        UserDTO dto = new UserDTO(0, null, null);
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);

        assertEquals(id, dto.getId());
        assertEquals(name, dto.getName());
        assertEquals(email, dto.getEmail());
    }

    @Test
    void equalsAndHashCodeTest() {
        long id = 1212L;
        String name = "Anna";
        String email = "anna@mail.ru";

        UserDTO dto1 = new UserDTO(id, name, email);
        UserDTO dto2 = new UserDTO(id, name, email);
        UserDTO dto3 = new UserDTO(id, name, "other email");
        UserDTO dto4 = new UserDTO(id, "other name", email);
        UserDTO dto5 = new UserDTO(-21221L, name, email);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1, dto4);
        assertNotEquals(dto1, dto5);

    }

    @Test
    void setName() {
        long id = 1212L;
        String name = "Anna";
        String email = "anna@mail.ru";

        UserDTO dto = new UserDTO(id, null, email);
        dto.setName(name);

        assertEquals(name, dto.getName());
    }
}