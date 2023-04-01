package ru.practicum.ewm.main.dto;

import lombok.*;
import ru.practicum.ewm.main.common.Location;
import ru.practicum.ewm.main.model.enums.EventStateAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventUpdateDto {
    @Size(min = 3, max = 2000)
    private String annotation;

    @Size(min = 3, max = 7000)
    private String description;

    @Size(min = 3, max = 120)
    private String title;

    private Long categoryId;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
}
