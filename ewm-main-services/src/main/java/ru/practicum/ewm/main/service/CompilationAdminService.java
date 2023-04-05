package ru.practicum.ewm.main.service;

import ru.practicum.ewm.main.dto.CompilationDto;
import ru.practicum.ewm.main.dto.CompilationRequestDto;
import ru.practicum.ewm.main.dto.CompilationUpdateRequestDto;

public interface CompilationAdminService {
    CompilationDto addCompilation(CompilationRequestDto compilationDto);

    CompilationDto updateCompilation(CompilationUpdateRequestDto compilationDto, Long compId);

    void deleteCompilationById(Long compId);
}
