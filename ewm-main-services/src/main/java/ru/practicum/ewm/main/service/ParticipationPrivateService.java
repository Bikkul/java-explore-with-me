package ru.practicum.ewm.main.service;

import ru.practicum.ewm.main.dto.ParticipationDto;

import java.util.List;

public interface ParticipationPrivateService {
    ParticipationDto addNewRequest(Long userId, Long eventId);

    List<ParticipationDto> getRequestsByRequesterId(Long userId);

    ParticipationDto cancelRequestById(Long userId, Long requestId);
}
