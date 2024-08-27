package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserUpdateDTODeserTest {
    private final JacksonTester<UserUpdateDTO> json;

    @Test
    @DisplayName("deserialize UserCreateDTO")
    void deserializeUserCreateDTOTest() throws Exception {
        String name = "Anna";
        String email = "anna@mail.ru";
        String incomingJson = String.format("{\"name\": \"%s\",\"email\": \"%s\"}", name, email);
        UserUpdateDTO expectedObject = new UserUpdateDTO(name, email);
        assertThat(json.parse(incomingJson)).usingRecursiveComparison().isEqualTo(expectedObject);
    }

}