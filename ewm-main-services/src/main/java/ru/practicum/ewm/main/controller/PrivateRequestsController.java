package ru.practicum.ewm.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.dto.ParticipationDto;
import ru.practicum.ewm.main.service.ParticipationPrivateService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
public class PrivateRequestsController {
    private final ParticipationPrivateService participationPrivateService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationDto> getUserRequests(@PathVariable Long userId) {
        List<ParticipationDto> participations = participationPrivateService.getRequestsByRequesterId(userId);
        log.info("participation list of size={} has been got", participations.size());
        return participations;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationDto addUserRequest(@PathVariable Long userId,
                                           @RequestParam Long eventId) {
        ParticipationDto savedParticipation = participationPrivateService.addNewRequest(userId, eventId);
        log.info("participation with fields { " +
                        "id={}, " +
                        "eventId={}," +
                        "requesterId={}," +
                        "status={}," +
                        "created={} } has been saved", savedParticipation.getId(), savedParticipation.getEvent(),
                savedParticipation.getRequester(), savedParticipation.getStatus(), savedParticipation.getCreated());
        return savedParticipation;
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ParticipationDto cancelUserRequest(@PathVariable Long userId,
                                              @PathVariable Long requestId) {
        ParticipationDto canceledParticipation = participationPrivateService.cancelRequestById(userId, requestId);
        log.info("participation with fields { " +
                        "id={}, " +
                        "eventId={}," +
                        "requesterId={}," +
                        "status={}," +
                        "created={} } has been canceled", canceledParticipation.getId(), canceledParticipation.getEvent(),
                canceledParticipation.getRequester(), canceledParticipation.getStatus(), canceledParticipation.getCreated());
        return canceledParticipation;
    }
}
