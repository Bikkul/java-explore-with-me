package ru.practicum.ewm.main.service;

import ru.practicum.ewm.main.dto.CommentDto;
import ru.practicum.ewm.main.dto.CommentRequestDto;
import ru.practicum.ewm.main.model.enums.CommentSort;

import java.util.List;

public interface CommentPrivateService {
    CommentDto addNewCommentToEvent(CommentRequestDto commentRequestDto, Long userId);

    CommentDto updateEventComment(CommentRequestDto commentRequestDto, Long userId, Long commentId);

    void deleteEventComment(Long eventId, Long userId, Long commentId);

    List<CommentDto> getUserComments(Integer from, Integer size, Long userId, CommentSort commentSort);

    CommentDto getUserCommentById(Long userId, Long commentId);
}
