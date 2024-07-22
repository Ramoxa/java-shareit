package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.debug("Received request to create user with body: {}", userDto);
        UserDto createdUser = userService.create(userDto);
        log.info("Added user: {}", createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Validated(Update.class) @RequestBody UserDto userDto) {
        if ((userDto.getName() == null || userDto.getName().isBlank()) && (userDto.getEmail() == null || userDto.getEmail().isBlank())) {
            throw new BadRequestException("At least one of 'name' or 'email' must be provided");
        }

        UserDto updatedUser = userService.update(userDto, id);
        log.info("User updated: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity findAllUsers() {
        log.info("Fetched user list.");
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable Long id) {
        log.info("Fetched user with ID {}.", id);
        return ResponseEntity.ok(userService.getById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("Deleted user with ID {}.", id);
        userService.delete(id);
    }
}