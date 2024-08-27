package ru.practicum.shareit.item.dto.item;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemCreateDTOTest {
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
    void lengthTest() {
        long userId = 112L;
        long id = 10L;
        String name = "name";
        String description = "description";
        boolean available = true;
        ItemCreateDTO itemCreateDTO = new ItemCreateDTO(name, description, available, 10L);
        assertTrue(validator.validate(itemCreateDTO).isEmpty());
    }
}