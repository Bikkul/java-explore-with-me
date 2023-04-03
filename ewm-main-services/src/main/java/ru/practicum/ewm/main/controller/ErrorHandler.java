package ru.practicum.ewm.main.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.main.common.ApiError;
import ru.practicum.ewm.main.exception.*;

import javax.servlet.ServletException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({
            ServletException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            NumberFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError badRequestExceptionHandle(final Exception e) {
        ApiError apiError = ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request")
                .message(e.getMessage())
                .build();
        apiError.setErrors(e);
        return apiError;
    }

    @ExceptionHandler({UserNotFoundException.class,
            EventNotFoundException.class,
            CategoryNotFoundException.class,
            CompilationNotFoundException.class,
            ParticipationRequestsNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFoundExceptionHandle(final RuntimeException e) {
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
            NotValidEventToParticipationRequestException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError conflictExceptionHandle(final Exception e) {
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
