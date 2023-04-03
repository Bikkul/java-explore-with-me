package ru.practicum.ewm.main.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.*;
import ru.practicum.ewm.main.service.EventPrivateService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PrivateEventsController {
    private final EventPrivateService eventPrivateService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        List<EventShortDto> events = eventPrivateService.getUserEvents(from, size, userId);
        log.info("user events list with size={} has been got", events.size());
        return events;
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventsById(@PathVariable Long userId,
                                      @PathVariable Long eventId) {
        log.info("user event with userId={}, eventId={} has been got", userId, eventId);
        return eventPrivateService.getUserEventById(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationDto> getEventsRequests(@PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        List<ParticipationDto> eventsParticipation = eventPrivateService.getEventParticipationRequest(userId, eventId);
        log.info("events participation request list with size={} has been got", eventsParticipation.size());
        return eventsParticipation;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @RequestBody @Valid EventNewDto eventDto) {
        log.info(eventDto.getAnnotation(), eventDto.getTitle(), eventDto. getCategoryId());
        log.info("user with id={} added new event", userId);
        return eventPrivateService.addNewEvent(eventDto, userId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid EventUpdateDto eventDto) {
        EventFullDto updatedEvent = eventPrivateService.updateEvent(eventDto, userId, eventId);
        log.info("user event with userId={}, eventId={} has been update", userId, eventId);
        return updatedEvent;
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public EventParticipationStatusDto updateEventRequest(@PathVariable Long userId,
                                                          @PathVariable Long eventId,
                                                          @RequestBody @Valid EventParticipationStatusUpdateDto eventParticipationStatusDto) {
        EventParticipationStatusDto updateEventParticipationStatus = eventPrivateService.updateEventParticipationRequest(eventParticipationStatusDto, userId, eventId);
        log.info("participation statuses has been updated");
        return updateEventParticipationStatus;
    }
}
