package ru.practicum.ewm.main.exception;

public class EventNotValidStartDateException extends RuntimeException {
    public EventNotValidStartDateException(String message) {
        super(message);
    }
}
