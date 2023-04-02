package ru.practicum.ewm.main.service;

import ru.practicum.ewm.main.dto.EventFullDto;
import ru.practicum.ewm.main.dto.EventShortDto;
import ru.practicum.ewm.main.model.enums.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventPublicService {
    List<EventShortDto> searchEventsByUser(Integer from, Integer size, Set<Long> categoryIds, Boolean paid,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, String text, EventSort sort,
                                           Boolean onlyAvailable, HttpServletRequest request);

    EventFullDto getEventById(Long evenId, HttpServletRequest request);
}
