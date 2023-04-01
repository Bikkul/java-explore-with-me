package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.dto.UserDtoFromRequest;
import ru.practicum.ewm.main.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RestController("/admin/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class AdminUsersController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsersInfo(@RequestParam(required = false) Set<Long> ids,
                                      @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                      @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        List<UserDto> users = userService.getUsersByIds(ids, from, size);
        log.info("user list with size = {} has been got", users.size());
        return users;
    }

    @PostMapping
    @ResponseStatus()
    public UserDto addNewUser(@RequestBody UserDtoFromRequest userDto) {
        UserDto addedUser = userService.addNewUser(userDto);
        log.info("user with fields = { " +
                        "name = {}, " +
                        "email = {} " +
                        "} has been added",
                userDto.getName(), userDto.getEmail());
        return addedUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        log.info("user with id = {} has been deleted", userId);
    }
}
