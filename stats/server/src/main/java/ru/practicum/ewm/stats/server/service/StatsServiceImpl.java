package ru.practicum.ewm.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.common.dto.request.HitRequestDto;
import ru.practicum.ewm.stats.common.dto.response.HitResponseDto;
import ru.practicum.ewm.stats.common.dto.response.StatsResponseDto;
import ru.practicum.ewm.stats.server.model.Hit;
import ru.practicum.ewm.stats.server.model.Stat;
import ru.practicum.ewm.stats.server.repository.HitRepository;
import ru.practicum.ewm.stats.server.utils.HitDtoMapper;
import ru.practicum.ewm.stats.server.utils.StatsDtoMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final HitRepository hitRepository;

    @Override
    @Transactional
    public HitResponseDto addHit(HitRequestDto hitRequestDto) {
        Hit hit = HitDtoMapper.fromDto(hitRequestDto);
        Hit savedHit = hitRepository.save(hit);
        return HitDtoMapper.toDto(savedHit);
    }

    @Override
    public List<StatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (BooleanUtils.isTrue(unique)) {
            return getUniqueStats(start, end, uris);
        } else {
            return getNotUniqueStats(start, end, uris);
        }
    }

    private List<StatsResponseDto> getUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        if (uris == null || uris.size() == 0) {
            return mappedStats(hitRepository.getUniqueStatsFromHits(start, end));
        }
        return mappedStats(hitRepository.getUniqueStatsWithUrisFromHits(start, end, uris));
    }

    private List<StatsResponseDto> getNotUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        if (uris == null || uris.size() == 0) {
            return mappedStats(hitRepository.getStatsFromHits(start, end));
        }
        return mappedStats(hitRepository.getStatsWithUrisFromHits(start, end, uris));
    }

    private List<StatsResponseDto> mappedStats(List<Stat> hitRepository) {
        return hitRepository
                .stream()
                .map(StatsDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}