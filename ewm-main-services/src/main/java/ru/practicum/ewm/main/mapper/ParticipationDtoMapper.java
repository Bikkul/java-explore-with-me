package ru.practicum.ewm.main.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.main.dto.EventParticipationStatusDto;
import ru.practicum.ewm.main.dto.ParticipationDto;
import ru.practicum.ewm.main.model.Event;
import ru.practicum.ewm.main.model.Participation;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.enums.ParticipationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParticipationDtoMapper {
    public static ParticipationDto toParticipationDto(@NonNull Participation participation) {
        ParticipationDto participationDto = new ParticipationDto();

        Optional.ofNullable(participation.getId()).ifPresent(participationDto::setId);
        Optional.ofNullable(participation.getEvent())
                .ifPresent(event -> participationDto.setEvent(event.getId()));
        Optional.ofNullable(participation.getRequester())
                .ifPresent(requester -> participationDto.setRequester(requester.getUserId()));
        Optional.ofNullable(participation.getParticipationStatus()).ifPresent(participationDto::setStatus);
        Optional.ofNullable(participation.getCreatedOn()).ifPresent(participationDto::setCreated);
        return participationDto;
    }

    public static Participation toParticipation(@NonNull ParticipationDto participationDto, @NonNull Event event,
                                                @NonNull User requester) {
        Participation participation = new Participation();

        Optional.ofNullable(participationDto.getId()).ifPresent(participation::setId);
        Optional.ofNullable(participationDto.getCreated()).ifPresentOrElse(participation::setCreatedOn, LocalDateTime::now);
        Optional.ofNullable(participationDto.getStatus()).ifPresent(participation::setParticipationStatus);
        Optional.of(event).ifPresent(participation::setEvent);
        Optional.of(requester).ifPresent(participation::setRequester);
        return participation;
    }

    public static EventParticipationStatusDto toEventParticipationStatusDto(List<Participation> participations) {
        EventParticipationStatusDto eventParticipationStatusDto = new EventParticipationStatusDto();
        List<ParticipationDto> confirmedList = new ArrayList<>();
        List<ParticipationDto> rejectedList = new ArrayList<>();

        for (Participation participation : participations) {
            ParticipationDto participationDto = toParticipationDto(participation);

            if (participation.getParticipationStatus() == ParticipationStatus.CONFIRMED) {
                confirmedList.add(participationDto);
            } else if (participation.getParticipationStatus() == ParticipationStatus.REJECTED) {
                rejectedList.add(participationDto);
            }
        }
        eventParticipationStatusDto.setConfirmedRequests(confirmedList);
        eventParticipationStatusDto.setRejectedRequests(rejectedList);
        return eventParticipationStatusDto;
    }
}
