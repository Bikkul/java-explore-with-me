package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.dto.EventFullDto;
import ru.practicum.ewm.main.dto.EventShortDto;
import ru.practicum.ewm.main.model.enums.EventSort;
import ru.practicum.ewm.main.service.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController("/events")
@RequiredArgsConstructor
@Validated
public class PublicEventsController {
    private final EventPublicService eventPublicService;

    @GetMapping
    public List<EventShortDto> searchEvents(@RequestParam(required = false) Set<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @RequestParam(required = false) String text,
                                            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(required = false) EventSort sort,
                                            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                            HttpServletRequest request) {
        List<EventShortDto> events = eventPublicService.searchEventsByUser(from, size, categories, paid, rangeStart, rangeEnd, text, sort,
                onlyAvailable, request);
        log.info("events list with size={} has been got", events.size());
        return events;
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable Long id,
                                     HttpServletRequest httpServletRequest) {
        EventFullDto event = eventPublicService.getEventById(id, httpServletRequest);
        log.info("event with id={} has been got", id);
        return event;
    }
}
