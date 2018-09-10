package app.controllers;

import app.biz.AgendaBO;
import app.biz.AssociateBO;
import app.enums.TransactionCode;
import app.enums.VoteCode;
import app.exceptions.*;
import app.model.Agenda;
import app.model.Associate;
import app.model.Message;
import app.model.VotingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BOFacade {

    @Autowired
    private AgendaBO agendaBO;

    @Autowired
    private AssociateBO associateBO;

    public Message create(Agenda agenda){
        Message message = new Message();
        try {
             Agenda newAgenda = agendaBO.create(agenda);
             message.setMessage(TransactionCode.SUCCESS);
             message.setObject(newAgenda);
        }catch(IllegalArgumentException e){
            message.setMessage(e.getMessage());
            message.setErrorId(TransactionCode.INVALID_AGENDA.getCode());
        }catch(Exception e){
            message.setMessage(TransactionCode.ERROR);
        }
        return message;
    }

    public Message create(Associate associate){
        Message message = new Message();
        try {
            Associate newAssociate = associateBO.create(associate);
            message.setMessage(TransactionCode.SUCCESS);
            message.setObject(newAssociate);
        }catch(IllegalArgumentException e ){
            message.setMessage(e.getMessage());
            message.setErrorId(TransactionCode.INVALID_ASSOCIATE.getCode());
        }catch(Exception e){
            message.setMessage(e.getMessage());
        }
        return message;
    }

    public Message findAssociate(String associateId){
        Message message = new Message();
        try {
            Associate newAssociate = associateBO.findAssociate(associateId);
            message.setMessage(TransactionCode.SUCCESS);
            message.setObject(newAssociate);
        }catch(IllegalArgumentException e){
            message.setMessage(TransactionCode.ASSOCIATE_NOT_FOUND);
            message.setMessage(e.getMessage());
        }catch(Exception e){
            message.setMessage(TransactionCode.ERROR);
        }
        return message;

    }

    public Message setDuration(String id, int duration){
        Message message = new Message();
        try {
             Agenda agenda = agendaBO.setDuration(id, duration);
             message.setMessage(TransactionCode.SUCCESS);
             message.setObject(agenda);
        }
        catch(AgendaNotFoundException e){
            message.setMessage(e.getMessage());
            message.setErrorId(TransactionCode.AGENDA_NOT_FOUND.getCode());
        }catch(IllegalArgumentException e){
            message.setMessage(e.getMessage());
            message.setErrorId(TransactionCode.NEGATIVE_DURATION.getCode());
        }catch(DurationAlreadySetException e){
            message.setMessage(e.getMessage());
            message.setErrorId(TransactionCode.DURATION_ALREADY_SET.getCode());
        }catch(Exception e){
            message.setMessage(TransactionCode.ERROR);
        }
        return message;
    }

    public Message vote(String agendaId, String associateId, VoteCode voteCode){
        Message message = new Message();
        try {
            agendaBO.vote(agendaId, associateId, voteCode);
            message.setMessage(TransactionCode.SUCCESS);
        }catch(AgendaNotFoundException e){
            message.setMessage(e.getMessage());
            message.setErrorId(TransactionCode.AGENDA_NOT_FOUND.getCode());
        }catch(AgendaNotOpened e){
            message.setMessage(e.getMessage());
            message.setErrorId(TransactionCode.VOTING_NOT_STARTED.getCode());
        }catch(AgendaClosedForVotingException e){
            message.setMessage(e.getMessage());
            message.setErrorId(TransactionCode.VOTING_COMPLETED.getCode());
        }catch(AssociateNotFoundException e){
            message.setMessage(e.getMessage());
            message.setErrorId((TransactionCode.ASSOCIATE_NOT_FOUND).getCode());
        }catch(IllegalArgumentException e ){
            message.setMessage(e.getMessage());
            message.setErrorId(TransactionCode.ALREADY_VOTED.getCode());
        }catch(Exception e){
            message.setMessage(TransactionCode.ERROR);
        }
        return message;
    }

    public Message countVotes(String agendaId){
        Message message = new Message();
        try {
            VotingResult results = agendaBO.countVotes(agendaId);
            message.setMessage(TransactionCode.SUCCESS);
            message.setObject(results);
        }catch(IllegalArgumentException e){
            message.setMessage(TransactionCode.VOTING_NOT_COMPLETED);
            message.setMessage(e.getMessage());
        }catch(AgendaNotFoundException e){
            message.setMessage(TransactionCode.ERROR.AGENDA_NOT_FOUND);
            message.setMessage(e.getMessage());
        }catch(AgendaNotOpened e ){
            message.setMessage(TransactionCode.VOTING_NOT_STARTED);
            message.setMessage(e.getMessage());
        }catch(Exception e){
            message.setMessage(TransactionCode.ERROR);
        }
        return message;
    }

    public Agenda findAgenda(String agendaId){

        return agendaBO.findAgenda(agendaId);
    }

    public List<Agenda> findAll(){
        return agendaBO.findAll();
    }
}
