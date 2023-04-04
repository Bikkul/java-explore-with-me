package ru.practicum.ewm.main.mapper;

import org.springframework.lang.NonNull;
import ru.practicum.ewm.main.common.Location;
import ru.practicum.ewm.main.dto.EventFullDto;
import ru.practicum.ewm.main.dto.EventNewDto;
import ru.practicum.ewm.main.dto.EventShortDto;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.Event;
import ru.practicum.ewm.main.model.User;

import java.time.LocalDateTime;

public class EventDtoMapper {
    private EventDtoMapper() {
    }

    public static Event toEvent(@NonNull EventNewDto eventNewDto, @NonNull User initiator, @NonNull Category category) {
        Event event = new Event();

        event.setTitle(eventNewDto.getTitle());
        event.setDescription(eventNewDto.getDescription());
        event.setAnnotation(eventNewDto.getAnnotation());
        event.setEventDate(eventNewDto.getEventDate());
        event.setPaid(eventNewDto.getPaid());
        event.setRequestModeration(eventNewDto.getRequestModeration());
        event.setParticipantLimit(eventNewDto.getParticipantLimit());
        event.setLat(eventNewDto.getLocation().getLat());
        event.setLon(eventNewDto.getLocation().getLon());
        event.setCategory(category);
        event.setInitiator(initiator);
        return event;
    }

    public static EventFullDto toEventFullDto(@NonNull Event event, Long views, Long confirmedRequests) {
        EventFullDto eventFullDto = new EventFullDto();

        fillEventFullDtoWithoutViewsAndRequests(event, eventFullDto);
        eventFullDto.setViews(views);
        eventFullDto.setConfirmedRequests(confirmedRequests);

        return eventFullDto;
    }

    public static EventFullDto toEventFullDto(@NonNull Event event) {
        EventFullDto eventFullDto = new EventFullDto();

        fillEventFullDtoWithoutViewsAndRequests(event, eventFullDto);
        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(@NonNull Event event) {
        EventShortDto eventShortDto = new EventShortDto();

        fillEventShortDtoWithoutViewsAndRequests(event, eventShortDto);
        return eventShortDto;
    }

    public static EventShortDto toEventShortDto(@NonNull Event event, @NonNull Long views, @NonNull Long confirmedRequests) {
        EventShortDto eventShortDto = new EventShortDto();

        fillEventShortDtoWithoutViewsAndRequests(event, eventShortDto);
        eventShortDto.setViews(views);
        eventShortDto.setConfirmedRequests(confirmedRequests);
        return eventShortDto;
    }

    private static void fillEventShortDtoWithoutViewsAndRequests(Event event, EventShortDto eventShortDto) {
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setInitiator(UserDtoMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setCategory(CategoryDtoMapper.toCategoryDto(event.getCategory()));
    }

    private static void fillEventFullDtoWithoutViewsAndRequests(Event event, EventFullDto eventFullDto) {
        eventFullDto.setId(event.getId());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getEventState());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setLocation(Location.of(event));
        eventFullDto.setInitiator(UserDtoMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setCategory(CategoryDtoMapper.toCategoryDto(event.getCategory()));
        fillCreatedOn(event, eventFullDto);
    }

    private static void fillCreatedOn(Event event, EventFullDto eventFullDto) {
        LocalDateTime createdOn = event.getCreatedOn();
        if (createdOn == null) {
            LocalDateTime.now();
        } else {
            eventFullDto.setCreatedOn(createdOn);
        }
    }
}
