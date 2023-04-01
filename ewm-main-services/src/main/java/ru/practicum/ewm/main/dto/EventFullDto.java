package ru.practicum.ewm.main.dto;

import lombok.*;
import ru.practicum.ewm.main.common.Location;
import ru.practicum.ewm.main.model.enums.EventState;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventFullDto {
    private Long id;
    private Long confirmedRequests;
    private Long views;
    private Integer participantLimit;
    private String annotation;
    private String description;
    private String title;
    private CategoryDto category;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Boolean requestModeration;
    private EventState state;
    private LocalDateTime eventDate;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
}
