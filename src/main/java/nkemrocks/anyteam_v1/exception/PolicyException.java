package nkemrocks.anyteam_v1.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class PolicyException extends RuntimeException{
    private HttpStatusCode httpStatusCode;
    public PolicyException(HttpStatusCode httpStatusCode, String message){
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
