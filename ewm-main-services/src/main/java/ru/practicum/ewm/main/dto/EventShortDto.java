package ru.practicum.ewm.main.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class EventShortDto {
    private Long id;
    private String annotation;
    private Long confirmedRequests;
    private CategoryDto category;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
