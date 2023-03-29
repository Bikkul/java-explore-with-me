package ru.practicum.ewm.main.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
