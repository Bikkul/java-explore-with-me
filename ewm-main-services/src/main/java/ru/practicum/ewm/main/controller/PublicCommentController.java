package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.CommentDto;
import ru.practicum.ewm.main.model.enums.CommentSort;
import ru.practicum.ewm.main.service.CommentPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {
    private final CommentPublicService commentPublicService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getEventComments(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                             @RequestParam Long eventId,
                                             @RequestParam(required = false) CommentSort sort) {
        List<CommentDto> comments = commentPublicService.getEventComments(from, size, eventId, sort);
        log.info("comments with size={} has been got", comments.size());
        return comments;
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getEventCommentById(@PathVariable Long commentId,
                                          @RequestParam Long eventId) {
        CommentDto comment = commentPublicService.getEventCommentById(eventId, commentId);
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
