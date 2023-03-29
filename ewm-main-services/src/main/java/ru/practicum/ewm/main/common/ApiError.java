package ru.practicum.ewm.main.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ApiError {
    private String message;
    private HttpStatus status;
    private String reason;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder.Default
    private List<String> errors = new ArrayList<>();

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public void setErrors(Throwable throwable) {
        if (throwable == null) {
            return;
        }

        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
            errors.add(throwable.getMessage());
        }
    }
}
