package app.biz;


import app.enums.TransactionCode;
import app.enums.VoteCode;
import app.exceptions.*;
import app.model.*;
import app.repositories.AgendaRepository;
import app.utils.DateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
@Component
public class AgendaBO {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final String CLASS_NAME = this.getClass().getName();
    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private AssociateBO associateBO;

    public Agenda create(Agenda agenda) throws Exception{

        LOG.info(CLASS_NAME+" - Request to create Agenda "+agenda);
        Agenda newAgenda = null;
        agenda.setCreationDate(new Date());
        newAgenda = agendaRepository.save(agenda);

        return newAgenda;
    }

    public Agenda setDuration(String agendaId, int duration) throws DurationAlreadySetException,AgendaNotFoundException{
        LOG.info(CLASS_NAME+" - Request to update Agenda id:"+agendaId+", duration = "+duration);
        Agenda agenda = null;

        if (duration <= 0) {
               throw new IllegalArgumentException(TransactionCode.NEGATIVE_DURATION.getMessage());
        }

        agenda = findAgenda(agendaId);

        if (agenda == null) {
              throw new AgendaNotFoundException(agendaId);
        }

        if(agenda.getDuration()!=0){
            throw new DurationAlreadySetException(TransactionCode.DURATION_ALREADY_SET.getMessage(agenda.getId(),agenda.getTitle()));
        }

        agenda.setDuration(duration);
        agendaRepository.save(agenda);
        return agenda;
    }

    public Agenda findAgenda(String agendaId){
        LOG.info(CLASS_NAME+" - Request to find Agenda id "+agendaId);
        Agenda agenda = agendaRepository.findById(agendaId);
        return agenda;
    }

    public Agenda vote(String agendaId, String associateId, VoteCode vote) throws AgendaNotOpened, AgendaClosedForVotingException, AssociateNotFoundException, AgendaNotFoundException, IllegalArgumentException{
        LOG.info(CLASS_NAME+" - Request to register vote Agenda id"+agendaId+" ,associate "+associateId+", Vote "+vote);
        Agenda agenda = agendaRepository.findById(agendaId);
        if(agenda==null){
            throw new AgendaNotFoundException(agendaId);
        }
        if(isOpenForVotes(agenda) && validateAssociate(associateId)){
            if(agenda.hasVote(vote,associateId)){
                throw new IllegalArgumentException(TransactionCode.ALREADY_VOTED.getMessage(associateId));
            }
            LOG.info(CLASS_NAME+" - Agenda "+agendaId+" is open for votes, and associate "+associateId+" hasn't voted yet.");
            agenda.addVote(associateId,vote);
            agendaRepository.save(agenda);
        }
        else {
            throw new AgendaClosedForVotingException(TransactionCode.VOTING_COMPLETED.getMessage(agenda.getId(), agenda.getTitle(), DateHandler.addMinute(agenda.getCreationDate(),agenda.getDuration()).toString()));
        }
        return agenda;
    }

    public VotingResult countVotes(String agendaId) throws AgendaNotFoundException, AgendaNotOpened{
        LOG.info(CLASS_NAME+" - Received request to count votes for Agenda :"+agendaId);

        Agenda agenda = agendaRepository.findById(agendaId);
        VotingResult results = new VotingResult(agenda);
        if(agenda==null){
            throw new AgendaNotFoundException(agendaId);
        }
        if(isOpenForVotes(agenda)){
            throw new IllegalArgumentException(TransactionCode.VOTING_NOT_COMPLETED.getMessage(agendaId,agenda.getTitle()));
        }
        LOG.info(CLASS_NAME+" - Received request to count votes for Agenda :"+agendaId+" Results:"+results);

        return results;
    }

    public List<Agenda> findAll(){
        return agendaRepository.findAll();
    }



    private boolean isOpenForVotes(Agenda agenda) throws AgendaNotOpened{

        Date durationDate = DateHandler.addMinute(agenda.getCreationDate(), agenda.getDuration());
        Date currentDate = new Date();

        if(agenda.getDuration()==0)
            throw new AgendaNotOpened(TransactionCode.VOTING_NOT_STARTED.getMessage(agenda.getId(), agenda.getTitle()));

         return (durationDate.compareTo(currentDate)==1);

    }
    private boolean validateAssociate(String associateId) throws AssociateNotFoundException {
        Associate associate = associateBO.findAssociate(associateId);
        if(associate==null){
            throw new AssociateNotFoundException(TransactionCode.ASSOCIATE_NOT_FOUND.getMessage(associateId));
        }
        return true;
    }
}
