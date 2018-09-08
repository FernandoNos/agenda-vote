package app.exceptions;

public class AgendaClosedForVotingException extends  Exception{
    public AgendaClosedForVotingException(String message){
        super(message);
    }
}
