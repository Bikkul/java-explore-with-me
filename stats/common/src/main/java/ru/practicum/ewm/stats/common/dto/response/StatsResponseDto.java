package ru.practicum.ewm.stats.common.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class StatsResponseDto {
    private String app;
    private String uri;
    private Long hits;
}