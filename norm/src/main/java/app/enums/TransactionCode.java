package app.enums;

import java.util.List;

public enum TransactionCode {

    SUCCESS(100,"SUCCESS"),
    NEGATIVE_DURATION(-1,"The duration must be greater than 0!"),
    VOTING_COMPLETED(5,"The voting has been completed for Agenda %s - %s! Closed at %s"),
    AGENDA_NOT_FOUND(-10,"The Agenda was not found!"),
    ASSOCIATE_NOT_FOUND(-10,"The associate id %s was not found!"),
    ALREADY_VOTED(7, "You may only vote once, Associate %s!"),
    ERROR (-100,"An Error happened! Please reach out to your System Administrator!"),
    DURATION_ALREADY_SET(3, "You MAY NOT reset the duration of an Agenda (%s - %s)!"),
    VOTING_NOT_COMPLETED(4, "The Agenda %s - %s is still open for voting!")
    ;
    ;

    private  int code;
    private String message;

    private TransactionCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public void setCode(TransactionCode transactionCode){
        this.code = transactionCode.getCode();
    }
    public void setMessage(String messagae){
        this.message = message;
    }

    public int getCode(){return code;}
    public String getMessage(){return message;}
    public String getMessage(String... val){
        return String.format(message,val);
    }
}
