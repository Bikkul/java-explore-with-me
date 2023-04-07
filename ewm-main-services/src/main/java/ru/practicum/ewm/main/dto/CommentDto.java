package ru.practicum.ewm.main.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private Long commentator;
    private Long event;
    private LocalDateTime createdOn;
}
