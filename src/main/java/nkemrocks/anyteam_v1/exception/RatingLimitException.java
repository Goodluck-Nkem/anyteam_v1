package nkemrocks.anyteam_v1.exception;

public class RatingLimitException extends RuntimeException{
    public  RatingLimitException(String message){
        super(message);
    }
}
