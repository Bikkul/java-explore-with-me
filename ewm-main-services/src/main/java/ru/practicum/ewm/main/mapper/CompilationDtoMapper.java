package ru.practicum.ewm.main.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.main.dto.CompilationDto;
import ru.practicum.ewm.main.dto.CompilationRequestDto;
import ru.practicum.ewm.main.dto.EventShortDto;
import ru.practicum.ewm.main.model.Compilation;
import ru.practicum.ewm.main.model.Event;

import java.util.List;
import java.util.Set;

public class CompilationDtoMapper {
    private CompilationDtoMapper() {
    }

    public static Compilation toCompilation(@NonNull CompilationRequestDto compilationDto, @NonNull Set<Event> events) {
        Compilation compilation = new Compilation();

        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.getPinned());
        compilation.setEvents(events);
        return compilation;
    }

    public static CompilationDto toCompilationDto(@NonNull Compilation compilation, @NonNull List<EventShortDto> eventShortDtos) {
        CompilationDto compilationDto = new CompilationDto();

        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setEvents(eventShortDtos);
        return compilationDto;
    }
}
