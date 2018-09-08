package app.BOTests;

import app.biz.AgendaBO;
import app.biz.AssociateBO;
import app.controllers.BOFacade;
import app.enums.TransactionCode;
import app.enums.VoteCode;
import app.model.Associate;
import app.model.Message;
import app.model.Agenda;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@Component
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class VotingLogicTest {

    private List<Associate> associates;
    private List<Agenda> agendas;

    @Autowired
    private BOFacade boFacade;


    @Before
    public void createData(){
        createAgendas();
        createAssociates();
    }

    @Test
    public void positive_vote(){

        for(Agenda agenda : agendas){

            for(Associate associate : associates){
                int coin = ThreadLocalRandom.current().nextInt(0, VoteCode.values().length);
                VoteCode vote = VoteCode.values()[coin];
                boFacade.setDuration(agenda.getId(),10);
                boFacade.vote(agenda.getId(),associate.getId(),vote);
                agenda.addVote(associate.getId(),vote);
            }
        }

        for(Agenda agenda : agendas){
            Agenda agendaAux = boFacade.findAgenda(agenda.getId());
            for(VoteCode voteCode : VoteCode.values()){
                HashSet<String> votesDB = agendaAux.getListOfVotes(voteCode);
                HashSet<String> votesTest = agenda.getListOfVotes(voteCode);

                if(votesDB==null){
                    assertEquals(votesTest, null);
                }
                if(votesTest == null){
                    assertEquals(votesDB, null);
                }
                else {
                    assertEquals(votesDB.size(), votesTest.size());
                    assertTrue(votesDB.stream().allMatch(s->votesTest.contains(s)));
                }
            }
        }
    }
    @Test
    public void negative_already_voted(){
        createData();

        for(Agenda agenda : agendas){

            for(Associate associate : associates){
                int coin = ThreadLocalRandom.current().nextInt(0, VoteCode.values().length);
                VoteCode vote = VoteCode.values()[coin];
                Message aux = boFacade.vote(agenda.getId(),associate.getId(),vote);
                agenda.addVote(associate.getId(),vote);
                assertEquals(TransactionCode.SUCCESS.getCode(),aux.getCode());
                assertEquals(TransactionCode.SUCCESS.getMessage(),aux.getMessage());
            }

        }
        for(Agenda agenda : boFacade.findAll()){
            for(HashSet<String> votes : agenda.getVotes().values()){
                for(String associateId : votes){
                    int randomVote = ThreadLocalRandom.current().nextInt(0, VoteCode.values().length);
                    VoteCode vote = VoteCode.values()[randomVote];
                    Message aux = boFacade.vote(agenda.getId(),associateId,vote);
                    agenda.addVote(associateId,vote);
                    assertEquals(TransactionCode.ALREADY_VOTED.getCode(),aux.getCode());
                }
            }
        }
    }

    private void createAssociates(){
            associates = new ArrayList<Associate>();
            for(int i = 0;i<100;i++){
                Associate aux = new Associate();
                aux.setName("Associate "+i);
                Message ms =  boFacade.create(aux);
                associates.add((Associate)ms.getObject());
            }
        }

    private void createAgendas(){
        agendas = new ArrayList<Agenda>();
        for(int i=0;i<100;i++){
            Agenda agendaAux = new Agenda();
            agendaAux.setTitle("Title "+i);
            agendaAux.setDescription("Description "+i);
            agendaAux.setCreationDate(new Date());

            Message messageAux = boFacade.create(agendaAux);
            Agenda newAgenda = (Agenda)messageAux.getObject();
            boFacade.setDuration(newAgenda.getId(),10);
            agendas.add(newAgenda);

        }
    }
}

