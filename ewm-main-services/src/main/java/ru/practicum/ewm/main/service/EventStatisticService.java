package ru.practicum.ewm.main.service;

import ru.practicum.ewm.stats.common.dto.request.HitRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface EventStatisticService {
    void postHit(HitRequestDto hitRequestDto);

    Map<Long, Long> getEventsViews(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
