package app.BOTests;

import app.enums.TransactionCode;
import app.enums.VoteCode;
import app.model.Agenda;
import app.model.Associate;
import app.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RestAPIVotingLogicTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void positive_creation_to_votes(){
        Associate associate = new Associate();
        associate.setName("Test REST");
        LinkedHashMap linked;
        Message result = createAssociate(associate);
        linked = ((LinkedHashMap)result.getObject());
        assertEquals(result.getCode(), TransactionCode.SUCCESS.getCode());
        assertEquals(linked.get("name"),associate.getName());

        associate.setId((String)linked.get("id"));

        Agenda agenda = new Agenda();
        agenda.setTitle("Test Rest");
        agenda.setDescription("Test Description");
        result = createAgenda(agenda);
        linked = ((LinkedHashMap)result.getObject());
        assertEquals(result.getCode(), TransactionCode.SUCCESS.getCode());
        assertEquals(linked.get("agenda_title"),agenda.getTitle());
        assertEquals(linked.get("agenda_description"),agenda.getDescription());

        agenda.setId(((String)linked.get("id")));

        result = setDuration(agenda, "1");
        assertEquals(result.getCode(), TransactionCode.SUCCESS.getCode());

        result = vote(agenda.getId(),associate.getId(), "YES");
        assertEquals(result.getCode(), TransactionCode.SUCCESS.getCode());
        try {
            Thread.sleep(60 * 1000);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        result = getResults(agenda.getId());
        linked = ((LinkedHashMap)result.getObject());
        LinkedHashMap votes = (LinkedHashMap) linked.get("votes");
        assertEquals(votes.get("NO"),0);
        assertEquals(votes.get("YES"),1);
        assertEquals(linked.get("id"), agenda.getId());
    }

    /*
        TODO: Include tests with more values; Tests for the other negative scenarios (associate not found, agenda not found, agenda not open for votes, invalid duration, agenda closed, request result agenda closed
     */

    private Message getResults(String agendaId){
        ResultActions rest;
        MockHttpServletResponse response;
        String result;
        Message message;
        try {
            rest = this.mockMvc.perform(
                    get("http://localhost:8080/agenda/"+agendaId+"/result")
                            .contentType(MediaType.APPLICATION_JSON)
            );

            response = rest
                    .andReturn()
                    .getResponse();
            result = response.getContentAsString();
            message = convertStringToMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            message = null;
        }
        return message;
    }


    private Message vote(String agendaId,String associateId, String vote){
        ResultActions rest;
        MockHttpServletResponse response;
        String result;
        Message message;
        try {
            rest = this.mockMvc.perform(
                    put("http://localhost:8080/agenda/"+agendaId+"/vote")
                            .param("associate_id", associateId)
                            .param("vote",vote)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            response = rest
                    .andReturn()
                    .getResponse();
            result = response.getContentAsString();
            message = convertStringToMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            message = null;
        }
        return message;
    }


    private Message setDuration(Agenda agenda, String duration){
        ResultActions rest;
        MockHttpServletResponse response;
        String result;
        Message message;
        try {
            rest = this.mockMvc.perform(
                    put("http://localhost:8080/agenda/"+agenda.getId())
                            .param("duration", duration)
                            .contentType(MediaType.APPLICATION_JSON)
            );

            response = rest
                    .andReturn()
                    .getResponse();
            result = response.getContentAsString();
            message = convertStringToMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            message = null;
        }
        return message;
    }

    private Message createAgenda(Agenda agenda){
        ResultActions rest;
        MockHttpServletResponse response;
        String result;
        Message message;
        try {
            rest = this.mockMvc.perform(
                    post("http://localhost:8080/agenda/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"agenda_title\":\""+agenda.getTitle()+"\", \"agenda_description\":\""+agenda.getDescription()+"\"}")
            );

            response = rest
                    .andReturn()
                    .getResponse();
            result = response.getContentAsString();
            message = convertStringToMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
            message = null;
        }
        return message;
    }

    private Message createAssociate(Associate associate) {

        ResultActions rest;
        MockHttpServletResponse response;
        String result="";
        Message message=null;
        try {
            rest = this.mockMvc.perform(
                    post("http://localhost:8080/associate/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\":\""+associate.getName()+"\"}")
            );

            response = rest
                    .andReturn()
                    .getResponse();
            result = response.getContentAsString();
            message = convertStringToMessage(result);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            result = "ERROR";
        }
        return message;
    }

    private Message convertStringToMessage(String json){
        Message message = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            message = objectMapper.readValue(json, Message.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        return message;

    }

}
