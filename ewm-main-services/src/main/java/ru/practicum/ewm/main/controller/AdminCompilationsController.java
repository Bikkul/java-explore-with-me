package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.CompilationDto;
import ru.practicum.ewm.main.dto.CompilationRequestDto;
import ru.practicum.ewm.main.dto.CompilationUpdateRequestDto;
import ru.practicum.ewm.main.service.CompilationAdminService;

import javax.validation.Valid;

@RestController("/admin/compilations")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminCompilationsController {
    private final CompilationAdminService compilationAdminService;

    @PostMapping
    public CompilationDto addNewCompilations(@RequestBody @Valid CompilationRequestDto compilationRequestDto) {
        CompilationDto compilation = compilationAdminService.addCompilation(compilationRequestDto);
        log.info("new compilation with id={} has been added", compilation.getId());
        return compilation;
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilations(@PathVariable Long compId) {
        log.info("compilation with id={} has been deleted", compId);
        compilationAdminService.deleteCompilationById(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilations(@PathVariable Long compId,
                                             @RequestBody @Valid CompilationUpdateRequestDto compilationUpdateRequestDto) {
        CompilationDto updatedCompilation = compilationAdminService.updateCompilation(compilationUpdateRequestDto, compId);
        log.info("compilation with id={} has been updated", compId);
        return updatedCompilation;
    }
}
