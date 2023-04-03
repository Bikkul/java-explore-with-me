package ru.practicum.ewm.main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main.common.MyPageRequest;
import ru.practicum.ewm.main.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findAllByPinnedIs(Boolean pinned, Pageable pageable);

    List<Compilation> findAll(MyPageRequest myPageRequest);
}
