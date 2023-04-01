package ru.practicum.ewm.main.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.main.common.Location;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class EventNewDto {
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long categoryId;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @FutureOrPresent
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
