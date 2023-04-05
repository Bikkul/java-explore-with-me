package ru.practicum.ewm.main.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class CompilationRequestDto {
    @NotNull
    private Set<Long> events;

    @NotBlank
    @Size(max = 120)
    private String title;

    private Boolean pinned;
}
