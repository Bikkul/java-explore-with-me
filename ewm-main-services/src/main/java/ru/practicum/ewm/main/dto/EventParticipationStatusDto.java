package ru.practicum.ewm.main.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventParticipationStatusDto {
    List<ParticipationDto> confirmedRequests;
    List<ParticipationDto> rejectedRequests;
}
