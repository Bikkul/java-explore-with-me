package ru.practicum.ewm.main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.main.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT events " +
            "FROM Event as events " +
            "JOIN FETCH events.initiator " +
            "JOIN FETCH events.category " +
            "WHERE events.initiator.userId = ?1")
    List<Event> findAllByInitiatorId(Pageable pageable, Long initiatorId);
}
