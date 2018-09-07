package app.controllers;

import app.model.Associate;
import app.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/associate")
public class AssociateController {

    @Autowired
    private BOFacade boFacade;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Message add(@RequestBody (required = true) Associate associate) {
        return boFacade.create(associate);
    }
}
