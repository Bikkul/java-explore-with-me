package ru.practicum.ewm.main.exception;

public class NotValidEventToParticipationRequestException extends RuntimeException {
    public NotValidEventToParticipationRequestException(String message) {
        super(message);
    }
}
