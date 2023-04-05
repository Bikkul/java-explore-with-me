package ru.practicum.ewm.main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.main.model.Event;
import ru.practicum.ewm.main.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT events " +
            "FROM Event as events " +
            "JOIN FETCH events.initiator " +
            "JOIN FETCH events.category " +
            "WHERE events.initiator.userId = ?1")
    List<Event> findAllByInitiatorId(Pageable pageable, Long initiatorId);

    @Query("SELECT events " +
            "FROM Event as events " +
            "JOIN FETCH events.initiator " +
            "JOIN FETCH events.category " +
            "WHERE ((:userIds IS NULL) " +
            "       OR (events.initiator.userId IN (:userIds))) " +
            "AND ((:categoryIds IS NULL) " +
            "       OR (events.category.id IN (:categoryIds))) " +
            "AND ((:states IS NULL) " +
            "       OR (events.eventState IN (:states))) " +
            "AND (events.eventDate BETWEEN :start AND :end)")
    List<Event> findEventsByAdmin(@Param("userIds") Set<Long> userIds,
                                  @Param("categoryIds") Set<Long> categoryIds,
                                  @Param("states") Set<EventState> eventStates,
                                  @Param("start") LocalDateTime rangeStart,
                                  @Param("end") LocalDateTime rangeEnd,
                                  Pageable pageable);

    @Query("SELECT events " +
            "FROM Event as events " +
            "JOIN FETCH events.initiator " +
            "JOIN FETCH events.category " +
            "WHERE ((:categoryIds IS NULL) " +
            "       OR (events.category.id IN (:categoryIds))) " +
            "AND ((:paid IS NULL) " +
            "       OR (events.paid = :paid)) " +
            "AND ((:states IS NULL) " +
            "       OR (events.eventState = :states)) " +
            "AND (events.eventDate BETWEEN :start AND :end) " +
            "AND ((:text IS NULL) " +
            "       OR LOWER(events.description) LIKE LOWER(CONCAT('%',:text,'%')) " +
            "       OR LOWER(events.annotation) LIKE LOWER(CONCAT('%',:text,'%')))")
    List<Event> findEventsByUser(@Param("categoryIds") Set<Long> categoryIds,
                                 @Param("paid") Boolean paid,
                                 @Param("states") EventState eventStates,
                                 @Param("start") LocalDateTime rangeStart,
                                 @Param("end") LocalDateTime rangeEnd,
                                 @Param("text") String text,
                                 Pageable pageable);
}
