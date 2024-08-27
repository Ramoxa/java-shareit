package ru.practicum.shareit.item.dto.item;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemUpdateDTOTest {
    static ValidatorFactory validatorFactory;
    static Validator validator;

    @BeforeEach
    void initFilmController() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @AfterEach
    void closes() {
        validatorFactory.close();
    }

    @Test
    void updateTest() {
        long userId = 100;
        long itemId = 212;
        String name = "ssss";
        String description = "sssaqdewds";
        boolean available = true;
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO(name, description, available);
        assertTrue(validator.validate(itemUpdateDTO).isEmpty());
    }
}