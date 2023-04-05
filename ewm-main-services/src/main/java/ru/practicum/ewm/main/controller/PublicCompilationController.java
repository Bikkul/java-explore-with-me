package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.CompilationDto;
import ru.practicum.ewm.main.service.CompilationPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {
    private final CompilationPublicService compilationPublicService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        List<CompilationDto> compilations = compilationPublicService.getCompilationsByPinned(pinned, from, size);
        log.info("compilation list with size={} has been got", compilations.size());
        return compilations;
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationsById(@PathVariable Long compId) {
        CompilationDto compilation = compilationPublicService.getCompilationById(compId);
        log.info("compilation with id={} has been got", compId);
        return compilation;
    }
}
