package ru.practicum.ewm.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import ru.practicum.ewm.main.model.enums.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "events")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "event_annotation", nullable = false, length = 2000)
    private String annotation;

    @Column(name = "event_title", nullable = false, length = 120)
    private String title;

    @Column(name = "event_description", nullable = false, length = 7000)
    private String description;

    @Column(name = "event_location_lat", nullable = false)
    private Float lat;

    @Column(name = "event_location_lon", nullable = false)
    private Float lon;

    @Column(name = "event_paid")
    private Boolean paid = Boolean.FALSE;

    @Column(name = "event_request_moderation")
    private Boolean requestModeration = Boolean.TRUE;

    @Column
    @Enumerated(EnumType.STRING)
    private EventState eventState = EventState.PENDING;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_initiator_id")
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event_category_id")
    private Category category;

    @Column(name = "event_participant_limit")
    private Integer participantLimit = 0;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "event_created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "event_published_on")
    private LocalDateTime publishedOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Event event = (Event) o;
        return getId() != null && Objects.equals(getId(), event.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
