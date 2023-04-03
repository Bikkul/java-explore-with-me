package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.dto.CompilationDto;
import ru.practicum.ewm.main.service.CompilationPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController("/compilations")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {
    private final CompilationPublicService compilationPublicService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam Boolean pinned,
                                                @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        List<CompilationDto> compilations = compilationPublicService.getCompilationsByPinned(pinned, from, size);
        log.info("compilation list with size={} has been got", compilations.size());
        return compilations;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationsById(@PathVariable Long compId) {
        CompilationDto compilation = compilationPublicService.getCompilationById(compId);
        log.info("compilation with id={} has been got", compId);
        return compilation;
    }
}
