package ru.practicum.ewm.main.common;

import lombok.*;
import org.springframework.lang.NonNull;
import ru.practicum.ewm.main.model.Event;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private Float lat;
    private Float lon;

    public static Location of(@NonNull Event event) {
        return new Location(event.getLat(), event.getLon());
    }
}
