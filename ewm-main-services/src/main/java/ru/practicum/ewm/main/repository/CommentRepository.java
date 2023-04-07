package ru.practicum.ewm.main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.main.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCommentatorUserId(Long userId, Pageable pageable);

    Comment findByIdAndCommentatorUserId(Long commentId, Long userId);

    Comment findByIdAndEventId(Long commentId, Long eventId);

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);

    @Query("SELECT comments " +
            "FROM Comment AS comments " +
            "WHERE (comments.createdOn BETWEEN :start AND :END) " +
            "AND ((:users IS NULL) " +
            "       OR (comments.commentator.userId IN (:users))) " +
            "AND ((:events IS NULL) " +
            "       OR (comments.event.id IN (:events))) " +
            "AND ((:text IS NULL) " +
            "       OR LOWER(comments.text) LIKE LOWER(CONCAT('%',:text,'%')))")
    List<Comment> searchCommentsByParam(@Param("start") LocalDateTime rangeStart,
                               @Param("end") LocalDateTime rangeEnd,
                               @Param("users") Set<Long> userIds,
                               @Param("events") Set<Long> eventIds,
                               @Param("text") String text, Pageable pageable);
}
