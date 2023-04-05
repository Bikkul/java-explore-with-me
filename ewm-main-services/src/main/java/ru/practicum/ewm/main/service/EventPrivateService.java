package ru.practicum.ewm.main.service;

import ru.practicum.ewm.main.dto.*;

import java.util.List;

public interface EventPrivateService {
    EventFullDto addNewEvent(EventNewDto eventNewDto, Long userId);

    EventFullDto updateEvent(EventUpdateDto updateDtoFromUserRequest, Long userId, Long eventId);

    List<EventShortDto> getUserEvents(Integer from, Integer size, Long userId);

    EventFullDto getUserEventById(Long userId, Long eventId);

    List<ParticipationDto> getEventParticipationRequest(Long userId, Long eventId);

    EventParticipationStatusDto updateEventParticipationRequest(EventParticipationStatusUpdateDto eventParticipationStatusUpdateDto,
                                                     Long user, Long eventId);
}
