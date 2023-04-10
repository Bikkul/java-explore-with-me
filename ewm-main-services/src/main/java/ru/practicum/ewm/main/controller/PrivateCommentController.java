package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.CommentDto;
import ru.practicum.ewm.main.dto.CommentRequestDto;
import ru.practicum.ewm.main.model.enums.CommentSort;
import ru.practicum.ewm.main.service.CommentPrivateService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {
    private final CommentPrivateService commentPrivateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addCommentToEvent(@PathVariable Long userId,
                                        @RequestBody @Valid CommentRequestDto commentRequestDto) {
        CommentDto savedComment = commentPrivateService.addNewCommentToEvent(commentRequestDto, userId);
        log.info("comment with fields { " +
                        "id={}, " +
                        "text={}, " +
                        "event={}, " +
                        "commentator={}," +
                        "created={}" +
                        "} has been saved", savedComment.getId(), savedComment.getText(), savedComment.getEvent(),
                savedComment.getCommentator(), savedComment.getCreatedOn());
        return savedComment;
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @RequestBody @Valid CommentRequestDto commentDto) {
        CommentDto updatedComment = commentPrivateService.updateEventComment(commentDto, userId, commentId);
        log.info("comment with fields { " +
                        "id={}, " +
                        "text={}, " +
                        "event={}, " +
                        "commentator={}," +
                        "created={}" +
                        "} has been updated", updatedComment.getId(), updatedComment.getText(), updatedComment.getEvent(),
                updatedComment.getCommentator(), updatedComment.getCreatedOn());
        return updatedComment;
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId,
                              @RequestParam Long eventId) {
        commentPrivateService.deleteEventComment(eventId, userId, commentId);
        log.info("user with id={} at an event with id={} delete comment with id={}", userId, eventId, commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getUserComments(@PathVariable Long userId,
                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                            @RequestParam(required = false, defaultValue = "10") Integer size,
                                            @RequestParam(required = false) CommentSort sort) {
        List<CommentDto> comments = commentPrivateService.getUserComments(from, size, userId, sort);
        log.info("comments with size={} has been got", comments.size());
        return comments;
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getUserCommentById(@PathVariable Long userId,
                                         @PathVariable Long commentId) {
        CommentDto comment = commentPrivateService.getUserCommentById(userId, commentId);
        log.info("comment with fields { " +
                        "id={}, " +
                        "text={}, " +
                        "event={}, " +
                        "commentator={}," +
                        "created={}" +
                        "} has been got", comment.getId(), comment.getText(), comment.getEvent(),
                comment.getCommentator(), comment.getCreatedOn());
        return comment;
    }
}
