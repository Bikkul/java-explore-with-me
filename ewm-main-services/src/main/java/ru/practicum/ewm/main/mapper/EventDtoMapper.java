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
import java.util.Optional;

public class EventDtoMapper {
    public static Event toEvent(@NonNull EventNewDto eventNewDto, @NonNull User initiator, @NonNull Category category) {
        Event event = new Event();

        Optional.ofNullable(eventNewDto.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(eventNewDto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(eventNewDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(eventNewDto.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(eventNewDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventNewDto.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(eventNewDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventNewDto.getLocation().getLat()).ifPresent(event::setLat);
        Optional.ofNullable(eventNewDto.getLocation().getLon()).ifPresent(event::setLon);
        event.setCategory(category);
        event.setInitiator(initiator);
        return event;
    }

    public static EventFullDto toEventFullDto(@NonNull Event event, Long views, Long confirmedRequests) {
        EventFullDto eventFullDto = new EventFullDto();

        Optional.ofNullable(event.getId()).ifPresent(eventFullDto::setId);
        Optional.ofNullable(event.getTitle()).ifPresent(eventFullDto::setTitle);
        Optional.ofNullable(event.getParticipantLimit()).ifPresent(eventFullDto::setParticipantLimit);
        Optional.ofNullable(event.getAnnotation()).ifPresent(eventFullDto::setAnnotation);
        Optional.ofNullable(event.getDescription()).ifPresent(eventFullDto::setDescription);
        Optional.ofNullable(event.getPaid()).ifPresent(eventFullDto::setPaid);
        Optional.ofNullable(event.getRequestModeration()).ifPresent(eventFullDto::setRequestModeration);
        Optional.ofNullable(event.getEventState()).ifPresent(eventFullDto::setState);
        Optional.ofNullable(event.getEventDate()).ifPresent(eventFullDto::setEventDate);
        Optional.ofNullable(event.getCreatedOn()).ifPresentOrElse(eventFullDto::setCreatedOn, LocalDateTime::now);
        Optional.ofNullable(event.getPublishedOn()).ifPresent(eventFullDto::setPublishedOn);
        Optional.ofNullable(views).ifPresent(eventFullDto::setViews);
        Optional.ofNullable(confirmedRequests).ifPresent(eventFullDto::setConfirmedRequests);
        eventFullDto.setLocation(Location.of(event));
        eventFullDto.setInitiator(UserDtoMapper.toUserShortDto(event.getInitiator()));
        eventFullDto.setCategory(CategoryDtoMapper.toCategoryDto(event.getCategory()));

        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(@NonNull Event event, Long views, Long confirmedRequests) {
        EventShortDto eventShortDto = new EventShortDto();

        Optional.ofNullable(event.getId()).ifPresent(eventShortDto::setId);
        Optional.ofNullable(event.getAnnotation()).ifPresent(eventShortDto::setAnnotation);
        Optional.ofNullable(event.getEventDate()).ifPresent(eventShortDto::setEventDate);
        Optional.ofNullable(event.getPaid()).ifPresent(eventShortDto::setPaid);
        Optional.ofNullable(event.getTitle()).ifPresent(eventShortDto::setTitle);
        Optional.ofNullable(views).ifPresent(eventShortDto::setViews);
        Optional.ofNullable(confirmedRequests).ifPresent(eventShortDto::setConfirmedRequests);
        eventShortDto.setInitiator(UserDtoMapper.toUserShortDto(event.getInitiator()));
        eventShortDto.setCategory(CategoryDtoMapper.toCategoryDto(event.getCategory()));
        return eventShortDto;
    }
}
