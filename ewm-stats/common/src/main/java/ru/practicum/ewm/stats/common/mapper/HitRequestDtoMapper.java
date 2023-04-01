package ru.practicum.ewm.stats.common.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.stats.common.dto.request.HitRequestDto;

import java.time.LocalDateTime;

public class HitRequestDtoMapper {

    public static HitRequestDto toHitRequestDto(@NonNull String app, @NonNull String uri, @NonNull String ip) {
        HitRequestDto hitDto = new HitRequestDto();

        hitDto.setApp(app);
        hitDto.setUri(uri);
        hitDto.setIp(ip);
        hitDto.setTimestamp(LocalDateTime.now());
        return hitDto;
    }
}