package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void getName() {
        User user = new User(1L, "ss", "sss");
        assertEquals(1L, user.getId());
    }
}