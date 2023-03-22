package ru.practicum.ewm.stats.server.utils;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.stats.common.dto.response.StatsResponseDto;
import ru.practicum.ewm.stats.server.model.Stat;

public class StatsDtoMapper {

    public static StatsResponseDto toDto(@NonNull Stat stat) {
        StatsResponseDto statsDto = new StatsResponseDto();

        statsDto.setApp(stat.getApp());
        statsDto.setUri(stat.getUri());
        statsDto.setHits(stat.getHits());
        return statsDto;
    }
}