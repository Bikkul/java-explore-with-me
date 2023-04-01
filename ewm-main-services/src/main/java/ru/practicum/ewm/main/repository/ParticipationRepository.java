package ru.practicum.ewm.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main.model.Participation;
import ru.practicum.ewm.main.model.enums.ParticipationStatus;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Integer countAllByEventIdAndParticipationStatus(Long eventId, ParticipationStatus status);

    Boolean existsByEventIdAndRequesterUserId(Long eventId, Long requesterId);

    List<Participation> findAllByRequesterUserId(Long userId);

    List<Participation> findAllByEventInitiatorUserIdAndEventId(Long userId, Long eventId);

}
