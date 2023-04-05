package ru.practicum.ewm.main.exception;

public class NotValidUserToParticipationRequestException extends RuntimeException {
    public NotValidUserToParticipationRequestException(String message) {
        super(message);
    }
}
