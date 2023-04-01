package ru.practicum.ewm.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.common.Location;
import ru.practicum.ewm.main.common.MyPageRequest;
import ru.practicum.ewm.main.dto.*;
import ru.practicum.ewm.main.exception.*;
import ru.practicum.ewm.main.mapper.EventDtoMapper;
import ru.practicum.ewm.main.mapper.ParticipationDtoMapper;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.Event;
import ru.practicum.ewm.main.model.Participation;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.enums.EventState;
import ru.practicum.ewm.main.model.enums.EventStateAction;
import ru.practicum.ewm.main.model.enums.ParticipationStatus;
import ru.practicum.ewm.main.repository.CategoryRepository;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.ParticipationRepository;
import ru.practicum.ewm.main.repository.UserRepository;
import ru.practicum.ewm.main.service.EventPrivateService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPrivateServiceImpl implements EventPrivateService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRepository participationRepository;

    @Override
    @Transactional
    public EventFullDto addNewEvent(EventNewDto eventNewDto, Long userId) {
        checkValidStartEventDate(eventNewDto);
        Event event = getEvent(eventNewDto, userId);
        Event savedEvent = eventRepository.save(event);
        return EventDtoMapper.toEventFullDto(savedEvent, null, null);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(EventUpdateDto eventUpdateDto, Long userId, Long eventId) {
        checkUserExists(userId);
        checkValidStartEventDate(eventUpdateDto);
        Event event = getEvent(eventId);
        checkEventStatusIsPendingOrCanceled(event);
        performAction(event, eventUpdateDto);
        Event updatedEvent = getUpdateEvent(event, eventUpdateDto);
        return EventDtoMapper.toEventFullDto(updatedEvent, null, null);
    }

    @Override
    public List<EventShortDto> getUserEvents(Integer from, Integer size, Long userId) {
        checkUserExists(userId);
        return eventRepository.findAllByInitiatorId(MyPageRequest.of(from, size), userId)
                .stream()
                .map(event -> EventDtoMapper.toEventShortDto(event, null, null))
                .collect(toList());
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        checkUserExists(userId);
        Event event = getEvent(eventId);
        return EventDtoMapper.toEventFullDto(event, null, null);
    }

    @Override
    public List<ParticipationDto> getEventParticipationRequest(Long userId, Long eventId) {
        checkUserExists(userId);
        checkEventExists(eventId);
        return participationRepository.findAllByEventInitiatorUserIdAndEventId(userId, eventId)
                .stream()
                .map(ParticipationDtoMapper::toParticipationDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public EventParticipationStatusDto updateEventParticipationRequest(EventParticipationStatusUpdateDto eventParticipationStatusUpdateDto,
                                                                       Long userId, Long eventId) {
        checkUserExists(userId);
        Event event = getEvent(eventId);
        List<Participation> requests = getParticipationRequest(event, eventParticipationStatusUpdateDto);
        List<Participation> savedRequests = participationRepository.saveAll(requests);
        return ParticipationDtoMapper.toEventParticipationStatusDto(savedRequests);
    }

    private List<Participation> getParticipationRequest(Event event,
                                                        EventParticipationStatusUpdateDto eventParticipationStatusDto) {
        ParticipationStatus status = eventParticipationStatusDto.getStatus();
        int limit = event.getParticipantLimit();
        int confirmedRequests = participationRepository.countAllByEventIdAndParticipationStatus(event.getId(), ParticipationStatus.CONFIRMED);
        checkParticipationLimit(confirmedRequests, event.getParticipantLimit());

        List<Participation> requests = participationRepository.findAllById(eventParticipationStatusDto.getRequestsIds());
        setParticipationStatusToConfirmed(event, status, limit, confirmedRequests, requests);
        setParticipationStatusToRejected(status, requests);
        return requests;
    }

    private void setParticipationStatusToRejected(ParticipationStatus status, List<Participation> requests) {
        if (status == ParticipationStatus.REJECTED) {
            for (Participation request : requests) {
                checkParticipationStatusIsPending(request);
                request.setParticipationStatus(ParticipationStatus.REJECTED);
            }
        }
    }

    private void setParticipationStatusToConfirmed(Event event, ParticipationStatus status, int limit, int confirmedRequests,
                                                   List<Participation> requests) {
        if (status == ParticipationStatus.CONFIRMED) {
            for (Participation request : requests) {
                checkParticipationStatusIsPending(request);

                if (event.getParticipantLimit() == 0) {
                    request.setParticipationStatus(ParticipationStatus.CONFIRMED);
                    continue;
                }

                if (limit - confirmedRequests > 0) {
                    request.setParticipationStatus(ParticipationStatus.CONFIRMED);
                    confirmedRequests++;
                } else {
                    request.setParticipationStatus(ParticipationStatus.REJECTED);
                }
            }
        }
    }

    private Event getUpdateEvent(Event event, EventUpdateDto eventUpdateDto) {
        Long categoryId = eventUpdateDto.getCategoryId();
        Location location = eventUpdateDto.getLocation();

        if (categoryId != null) {
            checkCategoryExists(categoryId);
            Category category = categoryRepository.getReferenceById(categoryId);
            event.setCategory(category);
        }

        if (location != null) {
            event.setLat(location.getLat());
            event.setLon(location.getLon());
        }

        Optional.ofNullable(eventUpdateDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(eventUpdateDto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(eventUpdateDto.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(eventUpdateDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventUpdateDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventUpdateDto.getRequestModeration()).ifPresent(event::setRequestModeration);
        return event;
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    private Event getEvent(EventNewDto eventDto, Long userId) {
        Long categoryId = eventDto.getCategoryId();
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with id = %d not found", userId)));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(String.format("category with id=%d was not found", categoryId)));
        return EventDtoMapper.toEvent(eventDto, initiator, category);
    }

    private void performAction(Event event, EventUpdateDto eventUpdateDto) {
        EventStateAction stateAction = eventUpdateDto.getStateAction();
        if (stateAction == null) {
            return;
        }

        switch (stateAction) {
            case CANCEL_REVIEW:
                event.setEventState(EventState.CANCELED);
                break;
            case SEND_TO_REVIEW:
                event.setEventState(EventState.PENDING);
                break;
            default:
                throw new IllegalEventStateActionException(String.format("event state action expect CANCEL_REVIEW or SEND_TO_REVIEW, " +
                        "actual=%s", stateAction));
        }
    }

    private void checkCategoryExists(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(String.format("category with id=%d was not found", categoryId));
        }
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("user with id = %d not found", userId));
        }
    }

    private void checkEventExists(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException(String.format("event with id = %d not found", eventId));
        }
    }

    private void checkValidStartEventDate(EventNewDto eventNewDto) {
        if (!eventNewDto.getEventDate().minusHours(2L).isAfter(LocalDateTime.now())) {
            throw new EventNotValidStartDateException("Event date cannot be earlier than two hours from the current moment");
        }
    }

    private void checkValidStartEventDate(EventUpdateDto eventUpdateDto) {
        if (!eventUpdateDto.getEventDate().minusHours(2L).isAfter(LocalDateTime.now())) {
            throw new EventNotValidStartDateException("Event date cannot be earlier than two hours from the current moment");
        }
    }

    private void checkEventStatusIsPendingOrCanceled(Event event) {
        EventState eventState = event.getEventState();
        if (eventState == EventState.PUBLISHED) {
            throw new IllegalEventStateToUpdateExceptin("Only pending or canceled events can be changed");
        }
    }

    private void checkParticipationLimit(Integer confirmedRequests, Integer participationLimit) {
        int newParticipationRequest = 1;
        int requests = confirmedRequests + newParticipationRequest;

        if (requests >= participationLimit) {
            throw new ParticipationRequestsOutOfBoundsException("The participant limit has been reached");
        }
    }

    private void checkParticipationStatusIsPending(Participation request) {
        if (request.getParticipationStatus() != ParticipationStatus.PENDING) {
            throw new ParticipationRequestsStatusException("Request must have status PENDING");
        }
    }
}
