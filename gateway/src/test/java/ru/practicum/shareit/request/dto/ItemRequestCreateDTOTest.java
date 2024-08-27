package ru.practicum.shareit.request.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemRequestCreateDTOTest {
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
    void descriptionTest() {
        ItemRequestCreateDTO itemRequestCreateDTO = new ItemRequestCreateDTO("");
        assertFalse(validator.validate(itemRequestCreateDTO).isEmpty());
    }

    @Test
    void description2Test() {
        ItemRequestCreateDTO itemRequestCreateDTO = new ItemRequestCreateDTO("sss");
        assertTrue(validator.validate(itemRequestCreateDTO).isEmpty());
    }


}