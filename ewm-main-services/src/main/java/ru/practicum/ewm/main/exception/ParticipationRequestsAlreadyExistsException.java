package ru.practicum.ewm.main.exception;

public class ParticipationRequestsAlreadyExistsException extends RuntimeException {
    public ParticipationRequestsAlreadyExistsException(String message) {
        super(message);
    }
}
