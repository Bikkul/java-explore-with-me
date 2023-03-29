package ru.practicum.ewm.main.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.main.dto.UserDto;
import ru.practicum.ewm.main.dto.UserDtoFromRequest;
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

    public static User toUser(@NonNull UserDtoFromRequest userDto) {
        User user = new User();

        Optional.ofNullable(userDto.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(userDto.getName()).ifPresent(user::setName);
        return user;
    }
}
