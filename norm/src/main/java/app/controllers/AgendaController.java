package app.controllers;

import app.enums.VoteCode;
import app.model.Message;
import app.model.Agenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agenda")
public class AgendaController {

    @Autowired
    private BOFacade boFacade;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Message add(@RequestBody Agenda agenda) {
        return boFacade.create(agenda);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Message update(@PathVariable("id") String id, @RequestParam("duration") int duration) {
        return boFacade.setDuration(id, duration);
    }

    @RequestMapping(value = "/{id}/vote", method = RequestMethod.PUT)
    public Message vote(@PathVariable("id") String id, @RequestParam(value = "associate_id", required = true) String associateId,
                        @RequestParam(value = "vote", required = true) VoteCode voteCode){
       return boFacade.vote(id,associateId,voteCode);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Message countVotes (@PathVariable("id") String id){
        return boFacade.countVotes(id);
    }
}
