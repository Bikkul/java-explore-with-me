package ru.practicum.ewm.main.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.common.Location;
import ru.practicum.ewm.main.common.MyPageRequest;
import ru.practicum.ewm.main.dto.*;
import ru.practicum.ewm.main.exception.*;
import ru.practicum.ewm.main.mapper.EventDtoMapper;
import ru.practicum.ewm.main.mapper.HitRequestDtoMapper;
import ru.practicum.ewm.main.mapper.ParticipationDtoMapper;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.Event;
import ru.practicum.ewm.main.model.Participation;
import ru.practicum.ewm.main.model.User;
import ru.practicum.ewm.main.model.enums.*;
import ru.practicum.ewm.main.repository.CategoryRepository;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.ParticipationRepository;
import ru.practicum.ewm.main.repository.UserRepository;
import ru.practicum.ewm.main.service.EventAdminService;
import ru.practicum.ewm.main.service.EventPrivateService;
import ru.practicum.ewm.main.service.EventPublicService;
import ru.practicum.ewm.main.service.EventStatisticService;
import ru.practicum.ewm.stats.common.dto.request.HitRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventPrivateService, EventAdminService, EventPublicService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRepository participationRepository;
    private final EventStatisticService eventStatisticService;
    private static final String APP_NAME = "explore-with-me-main-services";

    @Override
    @Transactional
    public EventFullDto addNewEvent(EventNewDto eventNewDto, Long userId) {
        checkValidStartEventDate(eventNewDto);
        Event event = getEvent(eventNewDto, userId);
        Event savedEvent = eventRepository.save(event);
        return EventDtoMapper.toEventFullDto(savedEvent);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(EventUpdateDto eventUpdateDto, Long userId, Long eventId) {
        checkUserExists(userId);
        checkValidStartEventDate(eventUpdateDto);
        Event event = getEvent(eventId);
        checkEventStatusIsPendingOrCanceled(event);
        fillEventState(event, eventUpdateDto.getStateAction());
        Event eventToUpdate = getUpdateEventByUser(event, eventUpdateDto);
        Event updatedEvent = eventRepository.save(eventToUpdate);
        return EventDtoMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public List<EventShortDto> getUserEvents(Integer from, Integer size, Long userId) {
        checkUserExists(userId);
        return eventRepository.findAllByInitiatorId(MyPageRequest.of(from, size), userId)
                .stream()
                .map(EventDtoMapper::toEventShortDto)
                .collect(toList());
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        checkUserExists(userId);
        Event event = getEvent(eventId);
        return EventDtoMapper.toEventFullDto(event);
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
        participationRepository.saveAll(requests);
        return ParticipationDtoMapper.toEventParticipationStatusDto(requests);
    }

    @Override
    public List<EventFullDto> searchEventsByAdmin(Integer from, Integer size, Set<Long> userIds, Set<Long> categoryIds,
                                                  Set<EventState> eventStates, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        List<Event> events = eventRepository.findEventsByAdmin(userIds, categoryIds, eventStates, rangeStart, rangeEnd,
                MyPageRequest.of(from, size));
        Set<Long> eventsIds = getEventIds(events);
        List<String> eventsUri = getUris(eventsIds);
        var eventsViews = eventStatisticService.getEventsViews(rangeStart, rangeEnd, eventsUri, Boolean.FALSE);
        var confirmedRequests = participationRepository.getEventParticipationCount(eventsIds, ParticipationStatus.CONFIRMED);
        return events
                .stream()
                .map(event -> EventDtoMapper.toEventFullDto(event, eventsViews.get(event.getId()),
                        confirmedRequests.get(event.getId())))
                .sorted(Comparator.comparing(EventFullDto::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(EventAdminUpdateRequestDto eventDto, Long eventId) {
        Event event = getEvent(eventId);
        Event eventToUpdate = getUpdatedEventByAdmin(event, eventDto);
        checkEventAdminUpdate(event, eventDto.getStateAction());
        fillEventState(event, eventDto.getStateAction());
        Event updatedEvent = eventRepository.save(eventToUpdate);
        return EventDtoMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public List<EventShortDto> searchEventsByUser(Integer from, Integer size, Set<Long> categoryIds, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, String text,
                                                  EventSort sort, Boolean onlyAvailable, HttpServletRequest request) {
        List<Event> events = eventRepository.findEventsByUser(categoryIds, paid, EventState.PUBLISHED, rangeStart, rangeEnd,
                text, MyPageRequest.of(from, size));
        HitRequestDto hitRequestDto = HitRequestDtoMapper.toHitRequestDto(request, APP_NAME);
        eventStatisticService.postHit(hitRequestDto);
        Set<Long> eventsIds = getEventIds(events);
        List<String> eventsUri = getUris(eventsIds);
        var eventsViews = eventStatisticService.getEventsViews(rangeStart, rangeEnd, eventsUri, Boolean.FALSE);
        var confirmedRequests = participationRepository.getEventParticipationCount(eventsIds, ParticipationStatus.CONFIRMED);

        if (onlyAvailable == Boolean.TRUE) {
            events = selectOnlyAvailableEvents(events, eventsIds);
        }

        if (sort != null) {
            return getSortedEventsShortDto(events, eventsViews, confirmedRequests, sort);
        }
        return events.stream()
                .map(event -> EventDtoMapper.toEventShortDto(event, eventsViews.get(event.getId()),
                        confirmedRequests.get(event.getId())))
                .collect(toList());
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = getEvent(eventId);
        LocalDateTime rangeStart = event.getEventDate().minusYears(1L);
        LocalDateTime rangeEnd = event.getEventDate();
        List<String> requestURI = List.of(request.getRequestURI());
        var eventsViews = eventStatisticService.getEventsViews(rangeStart, rangeEnd, requestURI, Boolean.FALSE);
        var confirmedRequests = participationRepository.getEventParticipationCount(Set.of(eventId),
                ParticipationStatus.CONFIRMED);
        HitRequestDto hitRequestDto = HitRequestDtoMapper.toHitRequestDto(request, APP_NAME);
        eventStatisticService.postHit(hitRequestDto);
        return EventDtoMapper.toEventFullDto(event, eventsViews.get(eventId), confirmedRequests.get(eventId));
    }

    private List<EventShortDto> getSortedEventsShortDto(List<Event> events, Map<Long, Long> eventsViews,
                                                        Map<Long, Long> confirmedRequests, EventSort sort) {
        switch (sort) {
            case VIEWS:
                return events.stream()
                        .map(event -> EventDtoMapper.toEventShortDto(event, eventsViews.get(event.getId()),
                                confirmedRequests.get(event.getId())))
                        .sorted(Comparator.comparing(EventShortDto::getViews).reversed())
                        .collect(toList());
            case EVENT_DATE:
                return events.stream()
                        .map(event -> EventDtoMapper.toEventShortDto(event, eventsViews.get(event.getId()),
                                confirmedRequests.get(event.getId())))
                        .sorted(Comparator.comparing(EventShortDto::getEventDate).reversed())
                        .collect(toList());
            default:
                throw new IllegalEventSortParameterException(String
                        .format("sort parameter expect VIEWS or EVENT_DATE, actual: %s", sort));
        }
    }

    private List<String> getUris(Set<Long> eventIds) {
        List<String> uris = new ArrayList<>();
        for (Long eventId : eventIds) {
            uris.add("/events/" + eventId);
        }
        return uris;
    }

    private List<Participation> getParticipationRequest(Event event,
                                                        EventParticipationStatusUpdateDto eventParticipationStatusDto) {
        ParticipationStatus status = eventParticipationStatusDto.getStatus();
        int limit = event.getParticipantLimit();
        int confirmedRequests = participationRepository.getEventRequestsCount(event.getId(), ParticipationStatus.CONFIRMED);
        checkParticipationLimit(confirmedRequests, event.getParticipantLimit());

        List<Participation> requests = participationRepository.findAllById(eventParticipationStatusDto.getRequestIds());
        setParticipationStatusToConfirmed(event, status, limit, confirmedRequests, requests);
        setParticipationStatusToRejected(status, requests);
        return requests;
    }

    private Set<Long> getEventIds(List<Event> events) {
        Set<Long> eventIds = new HashSet<>();

        for (Event event : events) {
            eventIds.add(event.getId());
        }
        return eventIds;
    }

    private Event getUpdateEventByUser(Event event, EventUpdateDto eventUpdateDto) {
        Long categoryId = eventUpdateDto.getCategory();
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

        Optional.ofNullable(eventUpdateDto.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(eventUpdateDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(eventUpdateDto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(eventUpdateDto.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(eventUpdateDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventUpdateDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventUpdateDto.getRequestModeration()).ifPresent(event::setRequestModeration);
        return event;
    }

    private Event getUpdatedEventByAdmin(Event event, EventAdminUpdateRequestDto eventUpdateDto) {
        Long categoryId = eventUpdateDto.getCategory();
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

        Optional.ofNullable(eventUpdateDto.getTitle()).ifPresent(event::setTitle);
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
        Long categoryId = eventDto.getCategory();
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with id = %d not found", userId)));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(String.format("category with id=%d was not found", categoryId)));
        return EventDtoMapper.toEvent(eventDto, initiator, category);
    }

    private List<Event> selectOnlyAvailableEvents(List<Event> events, Set<Long> eventIds) {
        List<Event> onlyAvailableEvents = new ArrayList<>();
        var confirmedRequest = participationRepository.getEventParticipationCount(eventIds, ParticipationStatus.CONFIRMED);

        for (Event event : events) {
            Long eventId = event.getId();
            Integer participantLimit = event.getParticipantLimit();

            if (confirmedRequest.get(eventId) < participantLimit) {
                onlyAvailableEvents.add(event);
            }
        }
        return onlyAvailableEvents;
    }

    private void fillEventState(Event event, EventAdminStateAction stateAction) {
        if (stateAction == null) {
            return;
        }

        switch (stateAction) {
            case PUBLISH_EVENT:
                event.setEventState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case REJECT_EVENT:
                event.setEventState(EventState.CANCELED);
                break;
            default:
                throw new IllegalEventStateActionException(String.format("event state action expect PUBLISH_EVENT or REJECT_EVENT, " +
                        "actual=%s", stateAction));
        }
    }

    private void fillEventState(Event event, EventStateAction stateAction) {
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

    private static void checkValidEventPublishedDate(Event event) {
        LocalDateTime eventDate = event.getEventDate();
        LocalDateTime publishedOn = event.getPublishedOn();
        if (eventDate == null || publishedOn == null) {
            return;
        }

        if (!eventDate.minusHours(1L).isAfter(publishedOn)) {
            throw new EventNotValidStartDateException("Event date cannot be earlier than one hours from the published time");

        }
    }

    private void checkValidStartEventDate(EventUpdateDto eventUpdateDto) {
        if (!eventUpdateDto.getEventDate().minusHours(2L).isAfter(LocalDateTime.now())) {
            throw new EventNotValidStartDateException("Event date cannot be earlier than two hours from the current moment");
        }
    }

    private static void checkEventStateBeforePublish(Event event) {
        EventState eventState = event.getEventState();
        if (eventState != EventState.PENDING) {
            throw new IllegalEventStateActionException(String
                    .format("Cannot publish the event because it's not in the right state: %s", eventState));
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

    private static void checkEventAdminUpdate(Event event, EventAdminStateAction stateAction) {
        checkValidEventPublishedDate(event);

        if (stateAction == EventAdminStateAction.PUBLISH_EVENT) {
            checkEventStateBeforePublish(event);
        } else if (stateAction == EventAdminStateAction.REJECT_EVENT) {
            checkEventStateBeforeReject(event);
        }
    }

    private static void checkEventStateBeforeReject(Event event) {
        EventState eventState = event.getEventState();
        if (eventState == EventState.PUBLISHED) {
            throw new IllegalEventStateActionException(String
                    .format("Cannot cancel the event because it's not in the right state: %s", eventState));
        }
    }
}
