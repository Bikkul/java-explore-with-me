package ru.practicum.ewm.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.common.dto.request.HitRequestDto;
import ru.practicum.ewm.stats.common.dto.response.HitResponseDto;
import ru.practicum.ewm.stats.common.dto.response.HitStatsDto;
import ru.practicum.ewm.stats.server.model.Hit;
import ru.practicum.ewm.stats.server.repository.HitRepository;
import ru.practicum.ewm.stats.server.utils.HitDtoMapper;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<HitStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return BooleanUtils.isTrue(unique) ? getUniqueStats(start, end, uris) :
                getNotUniqueStats(start, end, uris);
    }

    private List<HitStatsDto> getUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        if (uris == null || uris.size() == 0) {
            return hitRepository.getUniqueStatsFromHits(start, end);
        }
        return hitRepository.getUniqueStatsWithUrisFromHits(start, end, uris);
    }

    private List<HitStatsDto> getNotUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        if (uris == null || uris.size() == 0) {
            return hitRepository.getStatsFromHits(start, end);
        }
        return hitRepository.getStatsWithUrisFromHits(start, end, uris);
    }
}