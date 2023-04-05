package ru.practicum.ewm.main.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.main.model.enums.ParticipationStatus;

@Setter
@Getter
@EqualsAndHashCode
public class EventParticipationCountDto {
    private Long eventId;
    private Long count;
    private ParticipationStatus status;

    public EventParticipationCountDto(Long eventId, Long count, ParticipationStatus status) {
        this.eventId = eventId;
        this.count = count;
        this.status = status;
    }
}
