package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> findAll() {
        log.info("Find all users");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto getUserDtoById(@PathVariable Long userId) {
        log.info("Find user with id {}", userId);
        return userService.getUserDtoById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                          @RequestBody UserDto userDto) {
        log.info("Update user with id {}", userId);
        return userService.update(userId, userDto);
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("Create user with id {}", userDto.getId());
        return userService.create(userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Delete user with id {}", userId);
        userService.delete(userId);
    }
}
