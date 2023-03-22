package ru.practicum.ewm.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.stats.server.model.Hit;
import ru.practicum.ewm.stats.server.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query(value = "SELECT NEW ru.practicum.ewm.stats.server.model.Stat(hit.app, hit.uri, COUNT(hit.ip)) " +
            "FROM Hit AS hit " +
            "WHERE (hit.timestamp BETWEEN ?1 AND ?2) " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(hit.ip) DESC")
    List<Stat> getStatsFromHits(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT NEW ru.practicum.ewm.stats.server.model.Stat(hit.app, hit.uri, COUNT(DISTINCT hit.ip)) " +
            "FROM Hit AS hit " +
            "WHERE (hit.timestamp BETWEEN ?1 AND ?2) " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(DISTINCT hit.ip) DESC")
    List<Stat> getUniqueStatsFromHits(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT NEW ru.practicum.ewm.stats.server.model.Stat(hit.app, hit.uri, COUNT(hit.ip)) " +
            "FROM Hit AS hit " +
            "WHERE (hit.timestamp BETWEEN ?1 AND ?2) " +
            "AND (hit.uri IN(?3)) " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(hit.ip) DESC")
    List<Stat> getStatsWithUrisFromHits(LocalDateTime start, LocalDateTime end, List<String> urls);

    @Query(value = "SELECT NEW ru.practicum.ewm.stats.server.model.Stat(hit.app, hit.uri, COUNT(DISTINCT hit.ip)) " +
            "FROM Hit AS hit " +
            "WHERE (hit.timestamp BETWEEN ?1 AND ?2) " +
            "AND (hit.uri IN(?3)) " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(DISTINCT hit.ip) DESC")
    List<Stat> getUniqueStatsWithUrisFromHits(LocalDateTime start, LocalDateTime end, List<String> urls);
}