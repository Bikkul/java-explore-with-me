package ru.practicum.ewm.main.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.main.model.enums.ParticipationStatus;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class EventParticipationStatusUpdateDto {
    private List<Long> requestsIds;
    private ParticipationStatus status;
}
