package ru.practicum.ewm.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.main.common.ApiError;
import ru.practicum.ewm.main.exception.CategoryNotFoundException;
import ru.practicum.ewm.main.exception.UserNotFoundException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        ApiError apiError = ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .build();
        apiError.setErrors(e);
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError userNotFoundException(final UserNotFoundException e) {
        ApiError apiError = ApiError
                .builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(e.getMessage())
                .build();
        apiError.setErrors(e);
        return apiError;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError categoryNotFoundException(final CategoryNotFoundException e) {
        ApiError apiError = ApiError
                .builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(e.getMessage())
                .build();
        apiError.setErrors(e);
        return apiError;
    }
}
