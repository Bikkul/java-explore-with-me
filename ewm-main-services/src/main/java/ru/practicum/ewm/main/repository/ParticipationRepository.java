package ru.practicum.ewm.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.main.dto.EventParticipationCountDto;
import ru.practicum.ewm.main.model.Participation;
import ru.practicum.ewm.main.model.enums.ParticipationStatus;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    @Query("SELECT COUNT(*) " +
            "FROM Participation AS requests " +
            "WHERE requests.event.id = ?1 " +
            "AND requests.participationStatus = ?2")
    Integer getEventRequestsCount(Long eventId, ParticipationStatus requestStatus);

    Boolean existsByEventIdAndRequesterUserId(Long eventId, Long requesterId);

    List<Participation> findAllByRequesterUserId(Long userId);

    List<Participation> findAllByEventInitiatorUserIdAndEventId(Long userId, Long eventId);

    @Query("SELECT NEW ru.practicum.ewm.main.dto.EventParticipationCountDto(participation.event.id, COUNT(*), participation.participationStatus) " +
            "FROM Participation as participation " +
            "WHERE (participation.event.id IN (:eventIds)) " +
            "AND (participation.participationStatus = :status) " +
            "GROUP BY participation.id")
    List<EventParticipationCountDto> findEventParticipation(@Param("eventIds") Set<Long> eventIds,
                                                                @Param("status") ParticipationStatus status);

    default Map<Long, Long> getEventParticipationCount(Set<Long> eventIds, ParticipationStatus status) {
        return findEventParticipation(eventIds, status).stream()
                .collect(Collectors.toMap(EventParticipationCountDto::getEventId, EventParticipationCountDto::getCount));
    }
}
