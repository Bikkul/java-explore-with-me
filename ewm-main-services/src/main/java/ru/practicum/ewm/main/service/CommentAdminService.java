package ru.practicum.ewm.main.service;

import ru.practicum.ewm.main.dto.CommentDto;
import ru.practicum.ewm.main.model.enums.CommentAdminSort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface CommentAdminService {
    List<CommentDto> searchComments(Integer from, Integer size, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    Set<Long> userIds, Set<Long> eventIds, String text, CommentAdminSort sort);

    void deleteCommentByAdmin(Long eventId, Long commentId);
}
