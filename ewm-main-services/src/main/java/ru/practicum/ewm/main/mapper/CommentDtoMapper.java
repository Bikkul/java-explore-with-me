package ru.practicum.ewm.main.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.main.dto.CommentDto;
import ru.practicum.ewm.main.dto.CommentRequestDto;
import ru.practicum.ewm.main.model.Comment;
import ru.practicum.ewm.main.model.Event;
import ru.practicum.ewm.main.model.User;

public class CommentDtoMapper {
    private CommentDtoMapper() {
    }

    public static Comment toComment(@NonNull CommentRequestDto commentRequestDto, @NonNull Event event, @NonNull User user) {
        Comment comment = new Comment();

        comment.setText(commentRequestDto.getText());
        comment.setCommentator(user);
        comment.setEvent(event);
        return comment;
    }

    public static CommentDto toCommentDto(@NonNull Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setEvent(comment.getEvent().getId());
        commentDto.setCommentator(comment.getCommentator().getUserId());
        commentDto.setCreatedOn(comment.getCreatedOn());
        return commentDto;
    }
}
