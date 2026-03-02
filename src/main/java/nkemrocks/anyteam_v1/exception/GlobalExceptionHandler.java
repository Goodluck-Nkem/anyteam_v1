package nkemrocks.anyteam_v1.exception;

import nkemrocks.anyteam_v1.dto.Error_DTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error_DTO> validationExceptionHandler(MethodArgumentNotValidException exception){
        String errorMessage = exception.getBindingResult()
                .getFieldErrors().stream().findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed!");
        return new ResponseEntity<>(new Error_DTO(errorMessage), HttpStatus.BAD_REQUEST); /* 400 */
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Error_DTO> serviceUnavailableExceptionHandler(ServiceUnavailableException exception){
        return new ResponseEntity<>(new Error_DTO(exception.getMessage()), HttpStatus.SERVICE_UNAVAILABLE); /* 503 */
    }

    @ExceptionHandler(SessionExpiredException.class)
    public ResponseEntity<Error_DTO> sessionExpiredExceptionHandler(SessionExpiredException exception){
        return new ResponseEntity<>(new Error_DTO(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
