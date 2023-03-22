package ru.practicum.ewm.stats.server.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Stat {
    private String app;
    private String uri;
    private Long hits;
}