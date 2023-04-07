package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.CommentDto;
import ru.practicum.ewm.main.model.enums.CommentAdminSort;
import ru.practicum.ewm.main.service.CommentAdminService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/comments")
@Slf4j
@RequiredArgsConstructor
public class AdminCommentController {
    private final CommentAdminService commentAdminService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> searchComments(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                           @RequestParam(required = false) Set<Long> userIds,
                                           @RequestParam(required = false) Set<Long> eventIds,
                                           @RequestParam(required = false) String text,
                                           @RequestParam(required = false) CommentAdminSort sort) {

        List<CommentDto> comments = commentAdminService.searchComments(from, size, rangeStart, rangeEnd, userIds, eventIds, text, sort);
        log.info("comments with size={} has been got", comments.size());
        return comments;
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId,
                              @RequestParam Long eventId) {
        commentAdminService.deleteCommentByAdmin(eventId, commentId);
        log.info("admin at an event with id={} delete comment with id={}", eventId, commentId);
    }
}
