package ru.practicum.ewm.main.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class CommentRequestDto {
    @NotBlank
    private String text;

    @NotNull
    private Long eventId;
}
