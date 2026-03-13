package nkemrocks.anyteam_v1.exception;

import jakarta.persistence.EntityNotFoundException;
import nkemrocks.anyteam_v1.dto.Error_DTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /* --- Built-in Runtime Exceptions --- */

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<Error_DTO> dataAccessResourceFailureExceptionHandler(DataAccessResourceFailureException exception) {
        return new ResponseEntity<>(
                new Error_DTO("The database is currently unreachable!"),
                HttpStatus.SERVICE_UNAVAILABLE /* 503 */
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Error_DTO> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException exception) {
        return new ResponseEntity<>(
                new Error_DTO("Database integrity violation occurred!"),
                HttpStatus.CONFLICT /* 409 */
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error_DTO> entityNotFoundExceptionHandler(Exception exception) {
        return new ResponseEntity<>(
                new Error_DTO("Requested record does not exist!"),
                HttpStatus.NOT_FOUND /* 404 */
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error_DTO> validationExceptionHandler(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult()
                .getFieldErrors().stream().findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed!");
        return new ResponseEntity<>(
                new Error_DTO(errorMessage),
                HttpStatus.BAD_REQUEST /* 400 */
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Error_DTO> missingRequestParamHandler(MissingServletRequestParameterException exception) {
        return new ResponseEntity<>(
                new Error_DTO("""
                        Detected Missing request parameter '%s' in the URL
                        """.formatted(exception.getParameterName())),
                HttpStatus.BAD_REQUEST /* 400 */
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Error_DTO> responseStatusExceptionHandler(ResponseStatusException exception) {
        return new ResponseEntity<>(
                new Error_DTO(exception.getMessage()),
                exception.getStatusCode()
        );
    }

    /* --- Custom Runtime Exceptions --- */

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Error_DTO> persistenceExceptionHandler(PersistenceException exception) {
        return new ResponseEntity<>(
                new Error_DTO(exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR /* 500 */
        );
    }

    @ExceptionHandler(RatingLimitException.class)
    public ResponseEntity<Error_DTO> ratingLimitExceptionHandler(RatingLimitException exception) {
        return new ResponseEntity<>(
                new Error_DTO(exception.getMessage()),
                HttpStatus.FORBIDDEN  /* 403 */
        );
    }

    @ExceptionHandler(ReplayAttemptException.class)
    public ResponseEntity<Error_DTO> replayAttemptExceptionHandler(ReplayAttemptException exception) {
        return new ResponseEntity<>(
                new Error_DTO(exception.getMessage()),
                HttpStatus.FORBIDDEN  /* 403 */
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Error_DTO> resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {
        return new ResponseEntity<>(
                new Error_DTO(exception.getMessage()),
                HttpStatus.NOT_FOUND /* 404 */
        );
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Error_DTO> serviceUnavailableExceptionHandler(ServiceUnavailableException exception) {
        return new ResponseEntity<>(
                new Error_DTO(exception.getMessage()),
                HttpStatus.SERVICE_UNAVAILABLE  /* 503 */
        );
    }

    @ExceptionHandler(SessionExpiredException.class)
    public ResponseEntity<Error_DTO> sessionExpiredExceptionHandler(SessionExpiredException exception) {
        return new ResponseEntity<>(
                new Error_DTO(exception.getMessage()),
                HttpStatus.FORBIDDEN  /* 403 */
        );
    }

    /* --- Generic exception --- */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error_DTO> genericExceptionHandler(Exception exception) {
        return new ResponseEntity<>(
                new Error_DTO(
                        "An Internal/Unexpected error occurred! :: "
                                + exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR /* 500 */
        );
    }

}
