package ru.practicum.ewm.stats.common.dto.response;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
public class HitStatsDto {
    private String app;
    private String uri;
    private Long hits;

    public HitStatsDto(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}