package ru.practicum.ewm.stats.server.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.stats.common.dto.request.HitRequestDto;
import ru.practicum.ewm.stats.common.dto.response.HitResponseDto;
import ru.practicum.ewm.stats.server.model.Hit;

import java.util.Optional;

public class HitDtoMapper {
    private HitDtoMapper() {
    }

    public static Hit fromDto(@NonNull HitRequestDto hitRequestDto) {
        Hit hit = new Hit();

        Optional.ofNullable(hitRequestDto.getApp()).ifPresent(hit::setApp);
        Optional.ofNullable(hitRequestDto.getUri()).ifPresent(hit::setUri);
        Optional.ofNullable(hitRequestDto.getIp()).ifPresent(hit::setIp);
        Optional.ofNullable(hitRequestDto.getTimestamp()).ifPresent(hit::setTimestamp);
        return hit;
    }

    public static HitResponseDto toDto(@NonNull Hit hit) {
        HitResponseDto hitDto = new HitResponseDto();

        hitDto.setId(hit.getId());
        hitDto.setApp(hit.getApp());
        hitDto.setUri(hit.getUri());
        hitDto.setIp(hit.getIp());
        hitDto.setTimestamp(hit.getTimestamp());
        return hitDto;
    }
}