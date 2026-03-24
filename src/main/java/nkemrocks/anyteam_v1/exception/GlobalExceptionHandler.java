package nkemrocks.anyteam_v1.exception;

import jakarta.persistence.EntityNotFoundException;
import nkemrocks.anyteam_v1.dto.status.Error_DTO;
import nkemrocks.anyteam_v1.util.ConstraintRelatedStrings;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.MimeType;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    /* --- Generic exception --- */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error_DTO> genericExceptionHandler(Exception exception) {
        Throwable currentCause = exception;
        StringBuilder exMsgBuilder = new StringBuilder();
        while (currentCause != null) {
            exMsgBuilder.append(" --- <")
                    .append(currentCause.getClass().getName())
                    .append("> :: ").append(currentCause.getMessage());
            currentCause = currentCause.getCause();
        }
        return new ResponseEntity<>(
                new Error_DTO("""
                        An Internal/Unexpected error occurred! =>%s"""
                        .formatted(exMsgBuilder)),
                HttpStatus.INTERNAL_SERVER_ERROR /* 500 */
        );
    }

    /* --- Spring-defined Runtime Exceptions --- */

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<Error_DTO> dataAccessResourceFailureExceptionHandler(DataAccessResourceFailureException exception) {
        return new ResponseEntity<>(
                new Error_DTO("The database is currently unreachable!"),
                HttpStatus.SERVICE_UNAVAILABLE /* 503 */
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Error_DTO> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException exception) {
        String errorMessage = null;
        Throwable currentCause = exception;
        while (currentCause != null) {
            /* -- if ConstraintViolationException, then return a specific or generic constraint violation message -- */
            if (currentCause instanceof ConstraintViolationException cve) {
                String constraintName = cve.getConstraintName() != null ? cve.getConstraintName().toLowerCase() : "";
                for (String key : ConstraintRelatedStrings.map.keySet()) {
                    /* specific constraint violation message */
                    if (constraintName.contains(key)) {
                        errorMessage = ConstraintRelatedStrings.map.get(key);
                        break;
                    }
                }

                /* generic constraint violation message */
                if (errorMessage == null)
                    errorMessage = "Database Constraint violation occurred!";
                break;
            }

            /* -- otherwise keep searching for cause -- */
            currentCause = currentCause.getCause();
        }

        /* -- return errorMessage or generic Integrity violation message -- */
        return new ResponseEntity<>(
                new Error_DTO(errorMessage != null ? errorMessage : "Database integrity violation occurred!"),
                HttpStatus.CONFLICT /* 409 */
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error_DTO> entityNotFoundExceptionHandler(EntityNotFoundException exception) {
        return new ResponseEntity<>(
                new Error_DTO("Requested data record does not exist!"),
                HttpStatus.NOT_FOUND /* 404 */
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Error_DTO> contentTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException exception) {
        List<String> supported = exception.getSupportedMediaTypes().stream()
                .map(MimeType::toString)
                .toList();
        return new ResponseEntity<>(
                new Error_DTO("""
                        Content-Type '%s' is not supported for this request. ==> Supported types: [ %s ]"""
                        .formatted(
                                exception.getContentType(),
                                supported.isEmpty() ? "" : String.join(", ", supported)
                        )),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE /* 415 */
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Error_DTO> messageNotReadableExceptionHandler(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(
                new Error_DTO("""
                        Received a malformed or invalid JSON request body that could not be parsed! --- \
                        Confirm your JSON, make sure syntax is correct, and that values are valid W.R.T the type. --- \
                        Pass valid UUID strings for IDs, number for numbers, string for strings, etc. --- \
                        In short, when you see this message, your JSON is the problem!"""),
                HttpStatus.BAD_REQUEST /* 400 */
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Error_DTO> requestMethodNotSupportedHandler(HttpRequestMethodNotSupportedException exception) {
        String[] supportedMethods = exception.getSupportedMethods();
        return new ResponseEntity<>(
                new Error_DTO("""
                        The '%s' method is not supported for this request. ==> Supported methods: [ %s ]"""
                        .formatted(
                                exception.getMethod(),
                                String.join(", ", supportedMethods)
                        )),
                HttpStatus.METHOD_NOT_ALLOWED /* 405 */
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
                        Detected Missing request parameter '%s' in the URL"""
                        .formatted(exception.getParameterName())),
                HttpStatus.BAD_REQUEST /* 400 */
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Error_DTO> noResourceFoundExceptionHandler(NoResourceFoundException exception) {
        return new ResponseEntity<>(
                new Error_DTO("""
                        The requested endpoint '%s' is not found!"""
                        .formatted(exception.getResourcePath()
                        )),
                HttpStatus.NOT_FOUND /* 404 */
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

    @ExceptionHandler(PolicyException.class)
    public ResponseEntity<Error_DTO> policyExceptionHandler(PolicyException exception) {
        return new ResponseEntity<>(
                new Error_DTO(exception.getMessage()),
                exception.getHttpStatusCode()
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

}
