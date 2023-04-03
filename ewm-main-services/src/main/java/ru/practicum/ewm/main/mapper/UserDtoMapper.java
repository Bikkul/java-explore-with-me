package ru.practicum.ewm.main.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.dto.UserRequestDto;
import ru.practicum.ewm.main.dto.UserShortDto;
import ru.practicum.ewm.main.model.User;

import java.util.Optional;

public class UserDtoMapper {
    public static UserDto toUserDto(@NonNull User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getUserId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User toUser(@NonNull UserRequestDto userDto) {
        User user = new User();

        Optional.ofNullable(userDto.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(userDto.getName()).ifPresent(user::setName);
        return user;
    }

    public static UserShortDto toUserShortDto(@NonNull User user) {
        UserShortDto userShortDto = new UserShortDto();

        userShortDto.setId(user.getUserId());
        userShortDto.setName(user.getName());
        return userShortDto;
    }
}
