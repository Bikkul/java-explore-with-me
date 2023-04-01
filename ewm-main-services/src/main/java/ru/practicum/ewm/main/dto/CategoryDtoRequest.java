package ru.practicum.ewm.main.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class CategoryDtoRequest {
    @NotBlank
    @Size(max = 64)
    private String name;
}
