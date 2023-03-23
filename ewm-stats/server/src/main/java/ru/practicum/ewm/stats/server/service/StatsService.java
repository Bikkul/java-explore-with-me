package ru.practicum.ewm.stats.server.service;

import ru.practicum.ewm.stats.common.dto.request.HitRequestDto;
import ru.practicum.ewm.stats.common.dto.response.HitResponseDto;
import ru.practicum.ewm.stats.common.dto.response.HitStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    HitResponseDto addHit(HitRequestDto hitRequestDto);

    List<HitStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}