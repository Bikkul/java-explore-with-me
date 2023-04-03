package ru.practicum.ewm.main.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.common.MyPageRequest;
import ru.practicum.ewm.main.dto.CompilationDto;
import ru.practicum.ewm.main.dto.CompilationRequestDto;
import ru.practicum.ewm.main.dto.CompilationUpdateRequestDto;
import ru.practicum.ewm.main.dto.EventShortDto;
import ru.practicum.ewm.main.exception.CompilationNotFoundException;
import ru.practicum.ewm.main.mapper.CompilationDtoMapper;
import ru.practicum.ewm.main.mapper.EventDtoMapper;
import ru.practicum.ewm.main.model.Compilation;
import ru.practicum.ewm.main.model.Event;
import ru.practicum.ewm.main.model.enums.ParticipationStatus;
import ru.practicum.ewm.main.repository.CompilationRepository;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.ParticipationRepository;
import ru.practicum.ewm.main.service.CompilationAdminService;
import ru.practicum.ewm.main.service.CompilationPublicService;
import ru.practicum.ewm.main.service.EventStatisticService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationAdminService, CompilationPublicService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventStatisticService eventStatisticService;
    private final ParticipationRepository participationRepository;

    @Override
    @Transactional
    public CompilationDto addCompilation(CompilationRequestDto compilationDto) {
        List<Event> eventList = eventRepository.findAllById(compilationDto.getEvents());
        Set<Event> eventsSet = new HashSet<>(eventList);
        Compilation compilation = CompilationDtoMapper.toCompilation(compilationDto, eventsSet);
        Compilation savedCompilation = compilationRepository.save(compilation);
        List<EventShortDto> eventsShortDto = getShortEventsDto(eventList);
        return CompilationDtoMapper.toCompilationDto(savedCompilation, eventsShortDto);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(CompilationUpdateRequestDto compilationDto, Long compId) {
        checkCompilationExists(compId);
        List<Event> eventList = eventRepository.findAllById(compilationDto.getEvents());
        Compilation compilationToUpdate = getCompilationToUpdate(compilationDto, compId, eventList);
        Compilation updatedCompilation = compilationRepository.save(compilationToUpdate);
        List<EventShortDto> eventShortDto = getShortEventsDto(eventList);
        return CompilationDtoMapper.toCompilationDto(updatedCompilation, eventShortDto);
    }

    @Override
    @Transactional
    public void deleteCompilationById(Long compId) {
        checkCompilationExists(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        checkCompilationExists(compId);
        Compilation compilation = compilationRepository.getReferenceById(compId);
        List<Event> eventList = new ArrayList<>(compilation.getEvents());
        List<EventShortDto> eventShortDto = getShortEventsDto(eventList);
        return CompilationDtoMapper.toCompilationDto(compilation, eventShortDto);
    }

    @Override
    public List<CompilationDto> getCompilationsByPinned(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilationList = getCompilations(pinned, from, size);
        return compilationList.stream()
                .map(compilation -> CompilationDtoMapper.toCompilationDto(compilation,
                        getShortEventsDto(new ArrayList<>(compilation.getEvents()))))
                .collect(Collectors.toList());
    }

    private List<Compilation> getCompilations(Boolean pinned, Integer from, Integer size) {
        if (pinned != null) {
            return compilationRepository.findAllByPinnedIs(pinned, MyPageRequest.of(from, size));
        }
        return compilationRepository.findAllByQuery(MyPageRequest.of(from, size));
    }

    private Set<Long> getEventIds(List<Event> events) {
        Set<Long> eventIds = new HashSet<>();

        for (Event event : events) {
            eventIds.add(event.getId());
        }
        return eventIds;
    }

    private List<String> getUris(Set<Long> eventIds) {
        List<String> uris = new ArrayList<>();
        for (Long eventId : eventIds) {
            uris.add("/events/" + eventId);
        }
        return uris;
    }

    private void checkCompilationExists(Long compilationId) {
        if (!compilationRepository.existsById(compilationId)) {
            throw new CompilationNotFoundException(String.format("compilation with id=%d not found", compilationId));
        }
    }

    private List<EventShortDto> getShortEventsDto(@NonNull List<Event> events) {
        int lastEvent = events.size() - 1;
        LocalDateTime rangeStart = events.get(0).getEventDate().minusYears(1L);
        LocalDateTime rangeEnd = events.get(lastEvent).getEventDate();
        Set<Long> eventsIds = getEventIds(events);
        List<String> eventsUri = getUris(eventsIds);
        var eventsViews = eventStatisticService.getEventsViews(rangeStart, rangeEnd, eventsUri, Boolean.FALSE);
        var confirmedRequests = participationRepository.getEventParticipationCount(eventsIds, ParticipationStatus.CONFIRMED);

        return events.stream()
                .map(event -> EventDtoMapper.toEventShortDto(event, eventsViews.get(event.getId()),
                        confirmedRequests.get(event.getId())))
                .collect(Collectors.toList());
    }

    private Compilation getCompilationToUpdate(CompilationUpdateRequestDto compilationDto, Long compId, List<Event> events) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        Set<Event> eventSet = new HashSet<>(events);

        Optional.ofNullable(compilationDto.getTitle()).ifPresent(compilation::setTitle);
        Optional.ofNullable(compilationDto.getPinned()).ifPresent(compilation::setPinned);
        compilation.setEvents(eventSet);
        return compilation;
    }
}
