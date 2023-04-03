package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.EventAdminUpdateRequestDto;
import ru.practicum.ewm.main.dto.EventFullDto;
import ru.practicum.ewm.main.model.enums.EventState;
import ru.practicum.ewm.main.service.EventAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminEventsController {
    private final EventAdminService eventAdminService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> searchEvents(@RequestParam(required = false) Set<Long> users,
                                           @RequestParam(required = false) Set<EventState> states,
                                           @RequestParam(required = false) Set<Long> categories,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                           @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        List<EventFullDto> events = eventAdminService.searchEventsByAdmin(from, size, users, categories, states, rangeStart, rangeEnd);
        log.info("event list with size={} has been got", events.size());
        return events;
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody @Valid EventAdminUpdateRequestDto eventDto) {
        EventFullDto updatedEvent = eventAdminService.updateEventByAdmin(eventDto, eventId);
        log.info("event with id={} updated by admin", eventId);
        return updatedEvent;
    }
}
