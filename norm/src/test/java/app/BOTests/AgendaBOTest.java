package app.BOTests;

import app.biz.AgendaBO;
import app.controllers.BOFacade;
import app.enums.VoteCode;
import app.model.Associate;
import app.model.Message;
import app.model.Agenda;
import org.junit.After;
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
public class AgendaBOTest {

    private List<Agenda> agendas;

    @Autowired
    private BOFacade agendaBO;

    @Before
    public void createAgendas(){
        agendas = new ArrayList<Agenda>();
        for(int i=0;i<10;i++){
            Agenda agendaAux = new Agenda();
            agendaAux.setTitle("Title "+i);
            agendaAux.setDescription("Description "+i);
            agendaAux.setCreationDate(new Date());

            Message messageAux = agendaBO.create(agendaAux);
            agendas.add((Agenda)messageAux.getObject());
        }
    }

    @Test
    public void positive_findAgenda() {
        for(Agenda a : agendas) {
            Agenda agenda = agendaBO.findAgenda(a.getId());
            assertEquals(agenda.getId(), a.getId());
        }
    }

    @Test
    public void negative_findAgenda(){
        Agenda agenda = agendaBO.findAgenda("-1");
        assertEquals(agenda, null);
    }

    @Test
    public void positive_setDuration(){
        for(int i=0;i<agendas.size();i++) {
            Agenda agendaAux = agendas.get(i);
            int duration = ThreadLocalRandom.current().nextInt(1, 10);
            agendaBO.setDuration(agendaAux.getId(),duration);
            agendas.get(i).setDuration(duration);
        }

        for(int i=0;i<agendas.size();i++){
            Agenda agendaAux = agendaBO.findAgenda(agendas.get(i).getId());
            assertEquals(agendaAux.getDuration(), agendas.get(i).getDuration());
        }
    }



}
