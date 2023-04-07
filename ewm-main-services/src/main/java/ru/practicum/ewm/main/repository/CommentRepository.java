package ru.practicum.ewm.main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.main.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCommentatorUserId(Long userId, Pageable pageable);

    Optional<Comment> findByIdAndCommentatorUserId(Long commentId, Long userId);

    Optional<Comment>  findByIdAndEventId(Long commentId, Long eventId);

    List<Comment> findAllByEventId(Long eventId, Pageable pageable);

    @Query("SELECT comments " +
            "FROM Comment AS comments " +
            "JOIN FETCH comments.event " +
            "JOIN FETCH comments.commentator " +
            "WHERE ((:eventIds IS NULL) " +
            "       OR (comments.event.id IN (:eventIds))) " +
            "AND ((:userIds IS NULL) " +
            "       OR (comments.commentator IN (:userIds))) " +
            "AND (comments.createdOn BETWEEN :start AND :end) " +
            "AND ((:text IS NULL) " +
            "       OR LOWER(comments.text) LIKE LOWER(CONCAT('%',:commentText,'%')))")
    List<Comment> searchCommentsByParam(@Param("start") LocalDateTime rangeStart,
                                        @Param("end") LocalDateTime rangeEnd,
                                        @Param("userIds") Set<Long> userIds,
                                        @Param("eventIds") Set<Long> eventIds,
                                        @Param("commentText") String text, Pageable pageable);
}
