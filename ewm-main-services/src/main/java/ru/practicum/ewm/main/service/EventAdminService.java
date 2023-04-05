package ru.practicum.ewm.main.service;

import ru.practicum.ewm.main.dto.EventAdminUpdateRequestDto;
import ru.practicum.ewm.main.dto.EventFullDto;
import ru.practicum.ewm.main.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventAdminService {
    List<EventFullDto> searchEventsByAdmin(Integer from, Integer size, Set<Long> userIds, Set<Long> categoryIds,
                                           Set<EventState> eventStates, LocalDateTime rangeStart, LocalDateTime rangeEnd);

    EventFullDto updateEventByAdmin(EventAdminUpdateRequestDto eventDto, Long eventId);
}
