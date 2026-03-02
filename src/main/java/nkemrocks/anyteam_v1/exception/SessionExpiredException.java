package nkemrocks.anyteam_v1.exception;

public class SessionExpiredException extends RuntimeException{
    public SessionExpiredException(String message){
        super(message);
    }
}
