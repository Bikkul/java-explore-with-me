package ru.practicum.ewm.main.exception;

public class AccessPermissionDeniedException extends RuntimeException {
    public AccessPermissionDeniedException(String message) {
        super(message);
    }
}
