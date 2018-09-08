package app.exceptions;

public class AgendaNotFoundException extends Exception {
    public AgendaNotFoundException (String agendaId){

        super(String.format("The requested agenda (%s) was NOT found!", agendaId));
    }
}
