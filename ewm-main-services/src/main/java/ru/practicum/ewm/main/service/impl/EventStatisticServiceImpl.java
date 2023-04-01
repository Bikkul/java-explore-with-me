package ru.practicum.ewm.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.service.EventStatisticService;
import ru.practicum.ewm.stats.common.dto.request.HitRequestDto;
import ru.practicum.ewm.stats.common.dto.response.HitStatsDto;
import ru.practicum.stats.client.StatsClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventStatisticServiceImpl implements EventStatisticService {
    private final StatsClient statsClient;

    @Override
    @Transactional
    public void postHit(HitRequestDto hitRequestDto) {
        statsClient.postHit(hitRequestDto);
    }

    @Override
    public Map<Long, Long> getEventsViews(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<HitStatsDto> stats = statsClient.getStats(start, end, uris, unique);
        Map<Long, Long> eventsViewsMap = new HashMap<>();
        String eventUri = "/events/";

        for (HitStatsDto stat : stats) {
            String eventIdUri = stat.getUri().substring(eventUri.length());
            Long eventId = Long.parseLong(eventIdUri);
            eventsViewsMap.put(eventId, stat.getHits());
        }
        return eventsViewsMap;
    }
}
