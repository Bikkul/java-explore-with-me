package ru.practicum.ewm.main.service;

import ru.practicum.ewm.main.dto.CommentDto;
import ru.practicum.ewm.main.model.enums.CommentSort;

import java.util.List;

public interface CommentPublicService {
    List<CommentDto> getEventComments(Integer from, Integer size, Long eventId, CommentSort sort);

    CommentDto getEventCommentById(Long eventId, Long commentId);
}
