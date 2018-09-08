package app.exceptions;

import java.time.Duration;

public class DurationAlreadySetException extends Exception {
    public DurationAlreadySetException(String message){
        super(message);
    }
}
