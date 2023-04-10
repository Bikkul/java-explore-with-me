package ru.practicum.ewm.main.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.main.common.ApiError;
import ru.practicum.ewm.main.exception.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({
            ServletException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            NumberFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError badRequestExceptionHandle(final Exception e) {
        log.error("400 bad request exception reason:{}", e.getMessage(), e);
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
    public ApiError notFoundExceptionHandle(final EntityNotFoundException e) {
        log.error("404 not found exception reason:{}", e.getMessage(), e);
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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.error("500 undefined error reason:{}", e.getMessage(), e);
        ApiError apiError = ApiError
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("undefined error")
                .message(e.getMessage())
                .build();
        apiError.setErrors(e);
        return apiError;
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError dateIntegrityExceptionHandle(final DataIntegrityViolationException e) {
        log.error("409 data violation constraint exception reason:{}", e.getMessage(), e);
        ApiError apiError = ApiError
                .builder()
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated.")
                .message(e.getMessage())
                .build();
        apiError.setErrors(e);
        return apiError;
    }

    @ExceptionHandler({
            UserAlreadyExistException.class,
            EventNotValidStartDateException.class,
            IllegalEventStateActionException.class,
            IllegalEventSortParameterException.class,
            IllegalEventStateToUpdateExceptin.class,
            ParticipationRequestsAlreadyExistsException.class,
            ParticipationRequestsStatusException.class,
            ParticipationRequestsOutOfBoundsException.class,
            NotValidUserToParticipationRequestException.class,
            NotValidEventToParticipationRequestException.class,
            AccessPermissionDeniedException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError conflictExceptionHandle(final Exception e) {
        log.error("409 conflict exception reason:{}", e.getMessage(), e);
        ApiError apiError = ApiError
                .builder()
                .status(HttpStatus.CONFLICT)
                .reason("For the requested operation the conditions are not met")
                .message(e.getMessage())
                .build();
        apiError.setErrors(e);
        return apiError;
    }
}
