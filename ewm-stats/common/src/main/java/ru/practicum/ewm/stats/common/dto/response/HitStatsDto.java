package ru.practicum.ewm.stats.common.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class HitStatsDto {
    private String app;
    private String uri;
    private Long hits;
}