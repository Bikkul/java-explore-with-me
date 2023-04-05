package ru.practicum.ewm.main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.main.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findAllByPinnedIs(Boolean pinned, Pageable pageable);

    @Query("SELECT compilations " +
            "FROM Compilation as compilations " +
            "JOIN FETCH compilations.events")
    List<Compilation> findAllByQuery(Pageable pageable);
}
