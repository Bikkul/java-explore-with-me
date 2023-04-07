package ru.practicum.ewm.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.common.MyPageRequest;
import ru.practicum.ewm.main.dto.CommentDto;
import ru.practicum.ewm.main.dto.CommentRequestDto;
import ru.practicum.ewm.main.exception.AccessPermissionDeniedException;
import ru.practicum.ewm.main.exception.IllegalEventSortParameterException;
import ru.practicum.ewm.main.exception.IllegalEventStateActionException;
import ru.practicum.ewm.main.mapper.CommentDtoMapper;
import ru.practicum.ewm.main.model.Comment;
import ru.practicum.ewm.main.model.Event;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.enums.CommentAdminSort;
import ru.practicum.ewm.main.model.enums.CommentSort;
import ru.practicum.ewm.main.model.enums.EventState;
import ru.practicum.ewm.main.repository.CommentRepository;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.UserRepository;
import ru.practicum.ewm.main.service.CommentAdminService;
import ru.practicum.ewm.main.service.CommentPrivateService;
import ru.practicum.ewm.main.service.CommentPublicService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentPublicService, CommentPrivateService, CommentAdminService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> searchComments(Integer from, Integer size, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                           Set<Long> userIds, Set<Long> eventIds, String text, CommentAdminSort sort) {
        rangeStart = getValidRangeStartDateTime(rangeStart);
        rangeEnd = getValidRangeEndDateTime(rangeEnd);
        List<Comment> comments = commentRepository.searchCommentsByParam(rangeStart, rangeEnd, userIds, eventIds, text, MyPageRequest.of(from, size));

        if (sort != null) {
            return getSortedComments(comments, sort);
        }
        return comments
                .stream()
                .map(CommentDtoMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Long eventId, Long commentId) {
        checkEventExists(eventId);
        checkCommentExists(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentDto addNewCommentToEvent(CommentRequestDto commentRequestDto, Long userId) {
        Long eventId = commentRequestDto.getEventId();
        Event event = getEvent(eventId);
        checkEventToPublished(event);
        User user = getUser(userId);
        Comment comment = CommentDtoMapper.toComment(commentRequestDto, event, user);
        Comment savedComment = commentRepository.save(comment);
        return CommentDtoMapper.toCommentDto(savedComment);
    }

    @Override
    @Transactional
    public CommentDto updateEventComment(CommentRequestDto commentRequestDto, Long userId, Long commentId) {
        checkEventExists(commentRequestDto.getEventId());
        checkUserExists(userId);
        Comment comment = getComment(commentId);
        Long commentatorId = getComment(commentId).getCommentator().getUserId();
        checkValidUserComment(userId, commentatorId);
        Comment commentToUpdate = getCommentToUpdate(comment, commentRequestDto);
        Comment updatedComment = commentRepository.saveAndFlush(commentToUpdate);
        return CommentDtoMapper.toCommentDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteEventComment(Long eventId, Long userId, Long commentId) {
        checkEventExists(eventId);
        checkUserExists(userId);
        Long commentatorId = getComment(commentId).getCommentator().getUserId();
        checkValidUserComment(userId, commentatorId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getUserComments(Integer from, Integer size, Long userId, CommentSort commentSort) {
        checkUserExists(userId);

        if (commentSort != null) {
            return getSortedUserComments(from, size, userId, commentSort);
        }
        return commentRepository.findAllByCommentatorUserId(userId, MyPageRequest.of(from, size))
                .stream()
                .map(CommentDtoMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getUserCommentById(Long userId, Long commentId) {
        checkUserExists(userId);
        Comment userComment = commentRepository.findByIdAndCommentatorUserId(commentId, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("comment with id=%d not found", commentId)));
        return CommentDtoMapper.toCommentDto(userComment);
    }

    @Override
    public List<CommentDto> getEventComments(Integer from, Integer size, Long eventId, CommentSort sort) {
        checkEventExists(eventId);
        if (sort != null) {
            return getSortedEventComments(from, size, eventId, sort);
        }
        return commentRepository.findAllByEventId(eventId, MyPageRequest.of(from, size))
                .stream()
                .map(CommentDtoMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getEventCommentById(Long eventId, Long commentId) {
        checkEventExists(eventId);
        Comment comment = commentRepository.findByIdAndEventId(commentId, eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("comment with id=%d not found", commentId)));
        return CommentDtoMapper.toCommentDto(comment);
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("comment with id=%d not found", commentId)));
    }

    private void checkEventExists(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EntityNotFoundException(String.format("event with id=%d not found", eventId));
        }
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(String.format("user with id=%d not found", userId));
        }
    }

    private void checkValidUserComment(Long userId, Long commentatorId) {
        if (!commentatorId.equals(userId)) {
            throw new AccessPermissionDeniedException(String
                    .format("user with id=%d have not access to change comment, " +
                            "comment owner id=%d", userId, commentatorId));
        }
    }

    private void checkCommentExists(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotFoundException(String.format("comment with id=%d not found", commentId));
        }
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    private void checkEventToPublished(Event event) {
        EventState eventState = event.getEventState();
        if (eventState != EventState.PUBLISHED) {
            throw new IllegalEventStateActionException(String
                    .format("Cannot publish the comment because event is not published: %s", eventState));
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private Comment getCommentToUpdate(Comment comment, CommentRequestDto commentRequestDto) {
        comment.setText(commentRequestDto.getText());
        return comment;
    }

    private static LocalDateTime getValidRangeEndDateTime(LocalDateTime rangeEnd) {
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.MAX;
        }
        return rangeEnd;
    }

    private List<CommentDto> getSortedComments(List<Comment> comments, CommentAdminSort sort) {
        switch (sort) {
            case SORT_BY_USER:
                return comments
                        .stream()
                        .map(CommentDtoMapper::toCommentDto)
                        .sorted(Comparator.comparing(CommentDto::getCommentator))
                        .collect(Collectors.toList());
            case SORT_BY_EVENTS:
                return comments
                        .stream()
                        .map(CommentDtoMapper::toCommentDto)
                        .sorted(Comparator.comparing(CommentDto::getEvent))
                        .collect(Collectors.toList());
            case SORT_BY_TIME_ASC:
                return comments
                        .stream()
                        .map(CommentDtoMapper::toCommentDto)
                        .sorted(Comparator.comparing(CommentDto::getCreatedOn))
                        .collect(Collectors.toList());
            case SORT_BY_TIME_DESC:
                return comments
                        .stream()
                        .map(CommentDtoMapper::toCommentDto)
                        .sorted(Comparator.comparing(CommentDto::getCreatedOn).reversed())
                        .collect(Collectors.toList());
            default:
                throw new IllegalEventSortParameterException(String
                        .format("sort parameter expect SORT_BY_USER, SORT_BY_EVENTS, SORT_BY_TIME_ASC, SORT_BY_TIME_DESC " +
                                "actual: %s", sort));
        }
    }

    private static LocalDateTime getValidRangeStartDateTime(LocalDateTime rangeStart) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.MIN;
        }
        return rangeStart;
    }

    private List<CommentDto> getSortedUserComments(Integer from, Integer size, Long userId, CommentSort commentSort) {
        switch (commentSort) {
            case SORT_BY_TIME_ASC:
                return commentRepository.findAllByCommentatorUserId(userId, MyPageRequest.of(from, size))
                        .stream()
                        .map(CommentDtoMapper::toCommentDto)
                        .collect(Collectors.toList());
            case SORT_BY_TIME_DESC:
                return commentRepository.findAllByCommentatorUserId(userId, MyPageRequest.of(from, size))
                        .stream()
                        .map(CommentDtoMapper::toCommentDto)
                        .sorted(Comparator.comparing(CommentDto::getCreatedOn).reversed())
                        .collect(Collectors.toList());
            default:
                throw new IllegalEventSortParameterException(String
                        .format("sort parameter expect SORT_BY_TIME_ASC, SORT_BY_TIME_DESC " +
                                "actual: %s", commentSort));
        }
    }

    private List<CommentDto> getSortedEventComments(Integer from, Integer size, Long eventId, CommentSort sort) {
        switch (sort) {
            case SORT_BY_TIME_ASC:
                return commentRepository.findAllByEventId(eventId, MyPageRequest.of(from, size))
                        .stream()
                        .map(CommentDtoMapper::toCommentDto)
                        .collect(Collectors.toList());
            case SORT_BY_TIME_DESC:
                return commentRepository.findAllByEventId(eventId, MyPageRequest.of(from, size))
                        .stream()
                        .map(CommentDtoMapper::toCommentDto)
                        .sorted(Comparator.comparing(CommentDto::getCreatedOn).reversed())
                        .collect(Collectors.toList());
            default:
                throw new IllegalEventSortParameterException(String
                        .format("sort parameter expect SORT_BY_TIME_ASC, SORT_BY_TIME_DESC " +
                                "actual: %s", sort));
        }
    }
}
