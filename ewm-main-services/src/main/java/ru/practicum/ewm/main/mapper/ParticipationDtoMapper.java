package ru.practicum.ewm.main.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.main.dto.EventParticipationStatusDto;
import ru.practicum.ewm.main.dto.ParticipationDto;
import ru.practicum.ewm.main.model.Participation;
import ru.practicum.ewm.main.model.enums.ParticipationStatus;

import java.util.ArrayList;
import java.util.List;

public class ParticipationDtoMapper {
    private ParticipationDtoMapper() {
    }

    public static ParticipationDto toParticipationDto(@NonNull Participation participation) {
        ParticipationDto participationDto = new ParticipationDto();

        participationDto.setId(participation.getId());
        participationDto.setEvent(participation.getEvent().getId());
        participationDto.setRequester(participation.getRequester().getUserId());
        participationDto.setStatus(participation.getParticipationStatus());
        participationDto.setCreated(participation.getCreatedOn());
        return participationDto;
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
