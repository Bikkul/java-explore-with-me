package ru.practicum.ewm.stats.common.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
public class HitResponseDto {
    private Long id;
    private String uri;
    private String app;
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public HitResponseDto(Long id, String uri, String app, String ip, LocalDateTime timestamp) {
        this.id = id;
        this.uri = uri;
        this.app = app;
        this.ip = ip;
        this.timestamp = timestamp;
    }

    public HitResponseDto() {
    }
}