package nkemrocks.anyteam_v1.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class ControllerException extends RuntimeException{
    private HttpStatusCode httpStatusCode;
    public ControllerException(HttpStatusCode httpStatusCode, String message){
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
