package ru.practicum.ewm.main.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class CompilationUpdateRequestDto {
    @Size(max = 120)
    private String title;

    private Set<Long> events;
    private Boolean pinned;
}
