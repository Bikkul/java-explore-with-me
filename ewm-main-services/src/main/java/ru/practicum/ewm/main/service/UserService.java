package ru.practicum.ewm.main.service;

import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.dto.UserRequestDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<UserDto> getUsersByIds(Set<Long> ids, Integer from, Integer size);

    void deleteUser(Long id);

    UserDto addNewUser(UserRequestDto userRequestDto);
}
