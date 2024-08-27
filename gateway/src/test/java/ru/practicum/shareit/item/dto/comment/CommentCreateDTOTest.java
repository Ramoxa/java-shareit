package ru.practicum.shareit.item.dto.comment;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommentCreateDTOTest {
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
    void textTest() {
        CommentCreateDTO commentCreateDTO = new CommentCreateDTO(null);
        assertFalse(validator.validate(commentCreateDTO).isEmpty());
    }

    @Test
    void textTest2() {
        CommentCreateDTO commentCreateDTO = new CommentCreateDTO("ssssss");
        assertTrue(validator.validate(commentCreateDTO).isEmpty());
    }

}