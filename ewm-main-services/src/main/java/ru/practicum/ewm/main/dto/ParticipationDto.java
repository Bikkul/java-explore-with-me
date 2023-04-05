package ru.practicum.ewm.main.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.main.model.enums.ParticipationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ParticipationDto {
    private Long id;
    private Long event;
    private Long requester;
    private ParticipationStatus status;
    private LocalDateTime created;
}
