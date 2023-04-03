package ru.practicum.ewm.main.service;

import ru.practicum.ewm.main.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {
    CompilationDto getCompilationById(Long compId);

    List<CompilationDto> getCompilationsByPinned(Boolean pinned, Integer from, Integer size);
}
