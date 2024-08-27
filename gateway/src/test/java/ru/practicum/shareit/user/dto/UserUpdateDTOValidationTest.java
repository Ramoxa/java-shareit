package ru.practicum.shareit.user.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserUpdateDTOValidationTest {
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
    @DisplayName("Validation: correct name and email")
    void testValidationGoodNameAndEmail() {
        UserUpdateDTO userCreateDTO = UserUpdateDTO.builder()
                .name("K".repeat(10))
                .email("www@sss.ru")
                .build();
        assertTrue(validator.validate(userCreateDTO).isEmpty());
    }

    @Test
    @DisplayName("Validation: correct name and empty email")
    void testValidationGoodNameAndEmptyEmail() {
        UserUpdateDTO userCreateDTO = UserUpdateDTO.builder()
                .name("K".repeat(10))
                .email(null)
                .build();
        assertTrue(validator.validate(userCreateDTO).isEmpty());
    }

    @Test
    @DisplayName("Validation: empty name and correct email")
    void testValidationEmptyNameAndCorrectEmail() {
        UserUpdateDTO userCreateDTO = UserUpdateDTO.builder()
                .name(null)
                .email("www@sss.ru")
                .build();
        assertTrue(validator.validate(userCreateDTO).isEmpty());
    }

    @Test
    @DisplayName("Validation: empty name and empty email")
    void testValidationEmptyNameAndEmptyEmail() {
        UserUpdateDTO userCreateDTO = UserUpdateDTO.builder()
                .name(null)
                .email(null)
                .build();
        assertFalse(validator.validate(userCreateDTO).isEmpty());
    }


    @Test
    @DisplayName("Validation: too long name")
    void testValidationBadName() {
        UserUpdateDTO userCreateDTO = UserUpdateDTO.builder()
                .name("K".repeat(100))
                .email("www@sss.ru")
                .build();
        assertFalse(validator.validate(userCreateDTO).isEmpty());
    }

    @Test
    @DisplayName("Validation: incorrect email")
    void testValidationIncorrectEmail() {
        UserUpdateDTO userCreateDTO = UserUpdateDTO.builder()
                .name("K".repeat(10))
                .email("sssssssss")
                .build();
        assertFalse(validator.validate(userCreateDTO).isEmpty());
    }
}