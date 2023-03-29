package ru.practicum.ewm.main.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDtoFromRequest {
    @NotBlank
    @Length(max = 32)
    private String name;

    @Email
    @NotBlank
    @Length(max = 64)
    private String email;
}
