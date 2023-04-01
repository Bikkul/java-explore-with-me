package ru.practicum.ewm.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.dto.ParticipationDto;
import ru.practicum.ewm.main.exception.*;
import ru.practicum.ewm.main.mapper.ParticipationDtoMapper;
import ru.practicum.ewm.main.model.Event;
import ru.practicum.ewm.main.model.Participation;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.enums.EventState;
import ru.practicum.ewm.main.model.enums.ParticipationStatus;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.ParticipationRepository;
import ru.practicum.ewm.main.repository.UserRepository;
import ru.practicum.ewm.main.service.ParticipationPrivateService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationPrivateServiceImpl implements ParticipationPrivateService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;

    @Override
    @Transactional
    public ParticipationDto addNewRequest(Long userId, Long eventId) {
        User requester = getRequester(userId);
        Event event = getEvent(eventId);
        checkParticipationRequestToValid(requester, event);
        Participation participation = getParticipation(requester, event);
        Participation savedParticipation = participationRepository.save(participation);
        return ParticipationDtoMapper.toParticipationDto(savedParticipation);
    }

    @Override
    public List<ParticipationDto> getRequestsByRequesterId(Long userId) {
        checkUserExists(userId);
        return participationRepository.findAllByRequesterUserId(userId)
                .stream()
                .map(ParticipationDtoMapper::toParticipationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationDto cancelRequestById(Long userId, Long requestId) {
        checkUserExists(userId);
        Participation participation = getParticipationById(requestId);
        participation.setParticipationStatus(ParticipationStatus.CANCELED);
        Participation canceledParticipation = participationRepository.save(participation);
        return ParticipationDtoMapper.toParticipationDto(canceledParticipation);
    }

    private Participation getParticipationById(Long requestId) {
        return participationRepository.findById(requestId)
                .orElseThrow(() -> new ParticipationRequestsNotFoundException(
                        String.format("Participation with id=%d not found", requestId)));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    private User getRequester(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with id = %d not found", userId)));
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("user with id = %d not found", userId));
        }
    }

    private Participation getParticipation(User requester, Event event) {
        Participation participation = new Participation();
        Long eventId = event.getId();
        Long userId = requester.getUserId();

        checkParticipationRequestExists(eventId, userId);
        participation.setRequester(requester);
        participation.setEvent(event);

        if (event.getRequestModeration() == Boolean.FALSE) {
            participation.setParticipationStatus(ParticipationStatus.CONFIRMED);
        }
        return participation;
    }

    private void checkParticipationRequestExists(Long eventId, Long userId) {
        if (participationRepository.existsByEventIdAndRequesterUserId(eventId, userId)) {
            throw new ParticipationRequestsAlreadyExistsException("Request already exists");
        }
    }

    private void checkParticipationRequestToValid(User requester, Event event) {
        Long eventId = event.getId();
        Integer confirmedRequests = participationRepository
                .countAllByEventIdAndParticipationStatus(eventId, ParticipationStatus.CONFIRMED);
        Integer participationLimit = event.getParticipantLimit();

        checkEventToPublished(event.getEventState());
        checkValidRequesterToParticipation(requester, event);
        checkParticipationLimit(confirmedRequests, participationLimit);
    }

    private void checkParticipationLimit(Integer confirmedRequests, Integer participationLimit) {
        int newParticipationRequest = 1;
        int requests = confirmedRequests + newParticipationRequest;

        if (requests >= participationLimit) {
            throw new ParticipationRequestsOutOfBoundsException("Event has reached the limit of participation requests");
        }
    }

    private void checkValidRequesterToParticipation(User requester, Event event) {
        Long userId = requester.getUserId();
        Long initiatorId = event.getInitiator().getUserId();

        if (userId.equals(initiatorId)) {
            throw new NotValidUserToParticipationRequestException("Initiator of the event cannot add a request to participate in his event");
        }

    }

    private void checkEventToPublished(EventState eventState) {
        if (!eventState.equals(EventState.PUBLISHED)) {
            throw new NotValidEventToParticipationRequestException("It is forbidden to participate in an unpublished event");
        }
    }
}
