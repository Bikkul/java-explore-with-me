package ru.practicum.ewm.main.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.stats.common.dto.request.HitRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

public class HitRequestDtoMapper {
    private HitRequestDtoMapper() {
    }

    public static HitRequestDto toHitRequestDto(@NonNull HttpServletRequest request, String appName) {
        HitRequestDto hitDto = new HitRequestDto();

        hitDto.setApp(appName);
        hitDto.setUri(request.getRequestURI());
        hitDto.setIp(request.getRemoteAddr());
        hitDto.setTimestamp(LocalDateTime.now());
        return hitDto;
    }
}