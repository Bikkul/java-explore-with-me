package ru.practicum.ewm.main.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class UserDtoFromRequest {
    @NotBlank
    @Size(max = 32)
    private String name;

    @Email
    @NotBlank
    @Size(max = 64)
    private String email;
}
